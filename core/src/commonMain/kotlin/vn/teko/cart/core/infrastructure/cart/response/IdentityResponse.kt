package vn.teko.cart.core.infrastructure.cart.response

import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.CartTokenEntity

@Serializable
data class IdentityResponse(
    override val statusCode: Int = 0,
    override val error: String? = null,
    override val result: CartTokenEntity? = null
): BaseCartResponse<CartTokenEntity>()