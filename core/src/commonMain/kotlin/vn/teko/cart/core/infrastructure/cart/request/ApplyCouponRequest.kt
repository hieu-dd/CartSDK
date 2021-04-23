package vn.teko.cart.core.infrastructure.cart.request

import kotlinx.serialization.Serializable

@Serializable
data class ApplyCouponRequest(
    val coupon: String
)