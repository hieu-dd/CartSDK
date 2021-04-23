package vn.teko.cart.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderCouponsEntity(
    val applicableCoupons: MutableList<PromotionEntity> = mutableListOf(),
    val inapplicableCoupons: MutableList<PromotionEntity> = mutableListOf()
)