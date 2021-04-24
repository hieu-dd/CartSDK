package vn.teko.cart.domain.usecase.identity

import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.exception.CartIdentityError
import vn.teko.cart.domain.exception.Error
import vn.teko.cart.domain.model.CartTokenEntity
import vn.teko.cart.domain.model.CartTokenPayload
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.cart.CartDataSource
import vn.teko.cart.domain.util.isUUID
import vn.teko.cart.domain.util.randomUUIDString


open class GenerateCartToken(
    private val identityService: IdentityService
) : UseCase<GenerateCartToken.GenerateCartTokenParams, CartTokenEntity>() {

    override suspend fun run(params: GenerateCartTokenParams): CartTokenEntity {
        val payload = CartTokenPayload(iss = params.tenant, cid = params.cartId ?: randomUUIDString())
        val cartToken = identityService.encodeCartToken(payload)
        return CartTokenEntity(cartToken)
    }

    data class GenerateCartTokenParams(val tenant: String, val cartId: String?) :
        UseCase.Params() {
        override fun selfValidate(): Error? = super.selfValidate() ?: when {
            cartId != null && !cartId.isUUID() -> CartIdentityError.GenerateCartToken.InvalidParams
            else -> null
        }
    }
}
