package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.model.OrderCouponsEntity
import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.util.getOrElse

class GetCartCoupons(private val getCart: GetCart) :
    UseCase<GetCartCoupons.GetCartCouponsParams, OrderCouponsEntity>() {

    override suspend fun run(params: GetCartCouponsParams): OrderCouponsEntity = coroutineScope {
        val fullCart = getCart(GetCart.GetCartParam(params.cartId, params.userId, params.userToken, params.terminal))
            .getOrElse { throw it }
        fullCart.orderCoupons
    }

    data class GetCartCouponsParams(
        val cartId: String?,
        val userId: String?,
        val userToken: String?,
        val terminal: String,
    ) : Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId)
            )
    }
}