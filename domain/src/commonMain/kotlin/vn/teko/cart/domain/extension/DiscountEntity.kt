package vn.teko.cart.domain.extension

import vn.teko.cart.domain.model.PromotionEntity
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToLong


internal fun PromotionEntity.BenefitEntity.DiscountEntity.isValid(): Boolean {
    return this.isBudgetValid() && ((flat != null && flat!! > 0) || (percent != null && percent!! > 0))
}

internal fun PromotionEntity.BenefitEntity.DiscountEntity.getDiscountAsMoney(
    price: Long,
    productQuantity: Int? = null
): Long {
    val discountPerUnit = getDiscountPerUnit(price)

    return getRealDiscount(price, discountPerUnit, productQuantity)
}

internal fun PromotionEntity.BenefitEntity.DiscountEntity.getDiscountPerUnit(price: Long): Long = when {
    // TODO : verify with other services
    flat != null && flat!! > 0 -> flat!!.toLong()
    percent != null && percent!! > 0 -> {
        val discount = floor((percent!! / 100) * price)
        maxAmount?.let { min(it, discount).roundToLong() } ?: discount.roundToLong()
    }
    else -> 0L
}

internal fun PromotionEntity.BenefitEntity.DiscountEntity.getRealDiscount(
    price: Long,
    discountPerUnit: Long,
    productQuantity: Int? = null
): Long {
    var totalDiscount = discountPerUnit * (productQuantity ?: 1)
    totalDiscount = maxAmountPerOrder?.let { min(it, totalDiscount) } ?: totalDiscount

    if (productQuantity != null) {
        totalDiscount = ((totalDiscount / productQuantity) * productQuantity)
    }

    val totalPrice = price * (productQuantity ?: 1)

    return min(totalDiscount, totalPrice)
}
