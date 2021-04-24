package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.extension.applyCouponPromotion
import vn.teko.cart.domain.extension.getCurrentCoupon
import vn.teko.cart.domain.extension.toggleItem
import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.NumberValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.promotion.GetDetailCoupon
import vn.teko.cart.domain.util.getOrElse

open class UpdateCartItemsBySeller(
    private val cartDataSource: CartDataSource,
    private val importCart: ImportCart,
    private val getDetailCoupon: GetDetailCoupon
) : UseCase<UpdateCartItemsBySeller.UpdateCartItemsBySellerParams, Boolean>() {

    override suspend fun run(params: UpdateCartItemsBySellerParams): Boolean = coroutineScope {
        /**
         * Step 1: Find cart in DB by CartId or UserId
         */

        val cart = cartDataSource.getCart(params.cartId, params.userId)

        val fullCart = importCart(ImportCart.ImportCartParams(cart)).getOrElse { throw it }


        /**
         * Step 3: Find item in cart by itemId
         * If item is not available then throw an ItemNotFound exception
         */
        val items = fullCart.items.filter { it.sellerId == params.sellerId!! }

        /**
         * Toggle item
         */
        items.forEach {
            fullCart.toggleItem(it.lineItemId, params.selected)
        }

        /**
         * Apply coupon promotion.
         */

        getDetailCoupon(GetDetailCoupon.GetCouponDetailParams(fullCart, params.userToken, cart.getCurrentCoupon()))
            .getOrNull()?.let { promotion ->
                fullCart.applyCouponPromotion(promotion)
            }


        /**
         * Finally save current cart to db.
         */
        cartDataSource.saveCart(fullCart)
        true
    }

    data class UpdateCartItemsBySellerParams(
        val cartId: String?,
        val sellerId: Int?,
        val selected: Boolean,
        val userId: String?,
        val userToken: String?
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId),
                NumberValidator(sellerId, "sellerId", null, Int.MAX_VALUE, true)
            )
    }
}