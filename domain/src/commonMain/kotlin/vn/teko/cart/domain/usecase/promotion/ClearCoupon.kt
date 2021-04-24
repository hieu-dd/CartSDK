package vn.teko.cart.domain.usecase.promotion

import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.cart.CartDataSource

class ClearCoupon(
    private val cartDataSource: CartDataSource
) : UseCase<ClearCoupon.ClearCouponParams, Boolean>() {
    override suspend fun run(params: ClearCouponParams): Boolean {
        val cart = cartDataSource.getCart(params.cartId, params.userId)
        cartDataSource.deleteCouponPromotion(cart.id)
        return true
    }

    data class ClearCouponParams(
        val cartId: String?,
        val userId: String?
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId)
            )
    }
}