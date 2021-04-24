package vn.teko.cart.domain.usecase.cart

import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator

open class ClearCart(
    private val dataSource: CartDataSource
) : UseCase<ClearCart.ClearCartParams, Unit>() {

    override suspend fun run(params: ClearCartParams) {
        dataSource.deleteCart(params.cartId, params.userId.orEmpty())
    }

    data class ClearCartParams(val cartId: String?, val userId: String?) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId)
            )
    }
}
