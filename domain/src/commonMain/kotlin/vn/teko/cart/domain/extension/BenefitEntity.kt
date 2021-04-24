package vn.teko.cart.domain.extension

import vn.teko.cart.domain.model.PromotionEntity

/**
 * Whether a benefit is valid and
 *
 * This will check if the benefit (included money, items and voucher) is still active and not out of budget
 */
internal fun PromotionEntity.BenefitEntity?.isValid(): Boolean {
    if (this == null) return false
    if (discount == null && gifts.isNullOrEmpty() && voucher == null) {
        return false
    }

    val isValidMoney = discount?.isValid() ?: true
    val isValidItems = gifts?.isValid() ?: true
    val isValidVoucher = voucher?.isBudgetValid() ?: true

    return isValidItems && isValidMoney && isValidVoucher
}

/**
 * Whether a benefit has only money or not
 *
 * Return true if benefit has only money
 */
internal fun PromotionEntity.BenefitEntity?.hasOnlyMoney(): Boolean {
    return this?.gifts.isNullOrEmpty()
            && this?.voucher == null
            && this?.discount != null
}

/**
 * Whether a benefit has only gifts or not
 *
 * Return true if benefit has only gifts
 */
internal fun PromotionEntity.BenefitEntity.hasOnlyGift(): Boolean {
    return !gifts.isNullOrEmpty()
            && voucher == null
            && discount == null
}