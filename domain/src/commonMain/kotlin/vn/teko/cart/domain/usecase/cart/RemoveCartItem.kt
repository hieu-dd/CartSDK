package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.extension.applyCouponPromotion
import vn.teko.cart.domain.extension.getCurrentCoupon
import vn.teko.cart.domain.extension.removeProductItem
import vn.teko.cart.domain.usecase.CartItemValidator
import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.promotion.GetDetailCoupon
import vn.teko.cart.domain.util.getOrElse

open class RemoveCartItem(
    private val cartDataSource: CartDataSource,
    private val importCart: ImportCart,
    private val getDetailCoupon: GetDetailCoupon
) : UseCase<RemoveCartItem.RemoveLineItemParams, Unit>() {

    override suspend fun run(params: RemoveLineItemParams) = coroutineScope {
        val cart = cartDataSource.getCart(params.cartId, params.userId)
        val fullCartAsync = async {
            importCart(ImportCart.ImportCartParams(cart)).getOrElse { throw it }
        }

        val fullCart = fullCartAsync.await()
        val lineItem = fullCart.items.firstOrNull { it.lineItemId == params.lineItemId }
            ?: throw CartError.CartItemNotFound(params.lineItemId)
        fullCart.removeProductItem(lineItem)
        // Fetch coupon detail & apply it
        getDetailCoupon(GetDetailCoupon.GetCouponDetailParams(fullCart, params.userToken, cart.getCurrentCoupon()))
            .getOrNull()?.let {promotion->
                    fullCart.applyCouponPromotion(promotion)
            }
        cartDataSource.saveCart(fullCart)
    }

    data class RemoveLineItemParams(
        val cartId: String?,
        val userId: String?,
        val lineItemId: String,
        val userToken: String?
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId),
                CartItemValidator(lineItemId)
            )
    }
}