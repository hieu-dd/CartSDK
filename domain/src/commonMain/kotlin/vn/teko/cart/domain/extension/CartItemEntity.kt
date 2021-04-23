package vn.teko.cart.domain.extension

import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.PromotionEntity


/**
 * Update expectedPromotion to [selectedPromotion]
 *
 * @param selectedPromotion a selection promotion to apply for item
 */
internal fun CartItemEntity.updateExpectedPromotion(selectedPromotion: PromotionEntity?) {
    expectedPromotion = selectedPromotion
}

/**
 * Update expectedPromotion to [couponPromotion]
 *
 * @param couponPromotion a selection promotion to apply for item
 */
internal fun CartItemEntity.updateExpectedCouponPromotion(couponPromotion: PromotionEntity?) {
    expectedCouponPromotion = couponPromotion
}

/**
 * Check this line item has product, include product items
 *
 * @return true if it's product is not null and false if it is null
 */
internal fun CartItemEntity.hasProduct(): Boolean {
    return product != null
}

internal fun CartItemEntity.updateItemConsultationServiceFee(
    consultationServiceFee: Double
) {
    this.consultationServiceFee = consultationServiceFee
    calculatePrices()
}