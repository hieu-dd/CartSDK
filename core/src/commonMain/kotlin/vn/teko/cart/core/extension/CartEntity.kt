package vn.teko.cart.core.extension

import vn.teko.cart.domain.constants.APPLY_TYPE_ORDER
import vn.teko.cart.domain.constants.APPLY_TYPE_PART_ORDER
import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.model.CartPromotionEntity

fun CartEntity.getAppliedCoupon() =
    promotions.find { (it.applyType == APPLY_TYPE_ORDER || it.applyType == APPLY_TYPE_PART_ORDER) && !it.coupon.isNullOrBlank() }

fun CartEntity.getAppliedOrderPromotion() =
    promotions.find { it.applyType == APPLY_TYPE_ORDER && it.coupon.isNullOrBlank() }

