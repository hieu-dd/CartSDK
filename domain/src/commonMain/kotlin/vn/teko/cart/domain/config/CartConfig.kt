package vn.teko.cart.domain.config

data class ApplyPromotionConfig(
    val autoApplyCoupon: Boolean = true,
    val allowCouponWithGift: Boolean = false,
    val autoApplyOrderPromotion: Boolean = true
)
