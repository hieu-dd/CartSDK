package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.exception.Error
import vn.teko.cart.domain.extension.*
import vn.teko.cart.domain.model.CartEntity.Companion.MAX_ITEM_QUANTITY
import vn.teko.cart.domain.model.CartEntity.Companion.MIN_ITEM_QUANTITY
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.usecase.*
import vn.teko.cart.domain.usecase.promotion.GetDetailCoupon
import vn.teko.cart.domain.util.getOrElse

open class UpdateCartItem(
    private val cartDataSource: CartDataSource,
    private val importCart: ImportCart,
    private val getDetailCoupon: GetDetailCoupon
) : UseCase<UpdateCartItem.UpdateCartItemParams, CartItemEntity>() {

    override suspend fun run(params: UpdateCartItemParams) = coroutineScope {
        val cart = cartDataSource.getCart(params.cartId, params.userId)
        val fullCartAsync = async {
            importCart(ImportCart.ImportCartParams(cart)).getOrElse { throw it }
        }

        /**
         * Step 3: Find item in cart by itemId
         * If item is not available then throw an ItemNotFound exception
         */
        val fullCart = fullCartAsync.await()
        val item = fullCart.items.firstOrNull { it.lineItemId == params.lineItemId }
            ?: throw CartError.CartItemNotFound(params.lineItemId)

        /**
         * Toggle item
         */
        params.selected?.let {
            fullCart.toggleItem(params.lineItemId, it)
        }

        /** Validate stock
         * If stock is valid -> update item with this quantity
         * Else if quantity > totalAvailable >0 -> update item with total available & throw NotEnoughStockQuantity
         * Else -> remove item and throw OutOfStock
         */

        var stockError: Throwable? = null
        params.quantity?.let {
            val totalAvailable = item.product?.totalAvailable
            when {
                totalAvailable == null -> {
                    fullCart.updateItem(params.lineItemId, it)
                }
                totalAvailable == 0 -> {
                    stockError = CartError.OutOfStock
                    fullCart.removeProductItem(item)
                }
                totalAvailable < params.minTotalAvailableToCheck && totalAvailable < it -> {
                    stockError = CartError.NotEnoughStockQuantity
                    fullCart.updateItem(params.lineItemId, totalAvailable)
                }
                else -> {
                    fullCart.updateItem(params.lineItemId, it)
                }
            }
        }

        params.consultationServiceFee?.let {
            item.updateItemConsultationServiceFee(it)
        }

        // apply coupon
        getDetailCoupon(GetDetailCoupon.GetCouponDetailParams(fullCart, params.userToken, cart.getCurrentCoupon()))
            .getOrNull()?.let { promotion ->
                fullCart.applyCouponPromotion(promotion)
            }

        /**
         * Finally save current cart to db.
         */
        cartDataSource.saveCart(fullCart)
        stockError?.let { throw it } ?: fullCart.items.first { it.lineItemId == params.lineItemId }
    }

    data class UpdateCartItemParams(
        val cartId: String?,
        val userId: String?,
        val lineItemId: String,
        val quantity: Int?,
        val selected: Boolean?,
        val consultationServiceFee: Double?,
        val userToken: String?,
        val minTotalAvailableToCheck: Int
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId),
                CartItemValidator(lineItemId),
                NumberValidator(quantity, "quantity", MIN_ITEM_QUANTITY, MAX_ITEM_QUANTITY)
            )

        override fun selfValidate(): Error? = super.selfValidate() ?: when {
            (quantity == null && selected == null) ->
                CartError.UpdateCartItemError.InvalidParams
            else -> null
        }
    }
}