package vn.teko.cart.core.infrastructure.cart.response

import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.CartItemEntity

@Serializable
data class UpdateItemResponse(
    override val statusCode: Int = 0,
    override val error: String? = null,
    override val result: CartItemEntity? = null
) : BaseCartResponse<CartItemEntity>()