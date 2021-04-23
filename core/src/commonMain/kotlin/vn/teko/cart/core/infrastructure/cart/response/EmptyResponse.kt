package vn.teko.cart.core.infrastructure.cart.response

import kotlinx.serialization.Serializable

@Serializable
data class EmptyResponse(
    override val statusCode: Int = 0,
    override val error: String? = null,
    override val result: Unit? = Unit
): BaseCartResponse<Unit>()