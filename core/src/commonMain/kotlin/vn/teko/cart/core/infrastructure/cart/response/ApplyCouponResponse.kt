package vn.teko.cart.core.infrastructure.cart.response

import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.PromotionEntity

@Serializable
data class ApplyCouponResponse(
    override val statusCode: Int = 0,
    override val error: String? = null,
    override val result: PromotionEntity? = null
): BaseCartResponse<PromotionEntity>()