package vn.teko.cart.domain.comparators

import vn.teko.cart.domain.model.PromotionEntity


object MoneyComparator : Comparator<PromotionEntity.BenefitEntity.DiscountEntity> {
    override fun compare(
        a: PromotionEntity.BenefitEntity.DiscountEntity,
        b: PromotionEntity.BenefitEntity.DiscountEntity
    ): Int = when {
        a.flat != null && a.flat!! > 0 && b.flat != null && b.flat!! > 0
        -> DiscountTypeMoneyComparator.compare(a, b)
        a.percent != null && a.percent!! > 0 && b.percent != null && b.percent!! > 0
        -> DiscountTypePercentComparator.compare(a, b)
        a.percent != null && a.percent!! > 0 -> 1
        else -> -1
    }
}

object DiscountTypeMoneyComparator : Comparator<PromotionEntity.BenefitEntity.DiscountEntity> {
    override fun compare(
        a: PromotionEntity.BenefitEntity.DiscountEntity,
        b: PromotionEntity.BenefitEntity.DiscountEntity
    ) = 0
}

object DiscountTypePercentComparator : Comparator<PromotionEntity.BenefitEntity.DiscountEntity> {
    override fun compare(
        a: PromotionEntity.BenefitEntity.DiscountEntity,
        b: PromotionEntity.BenefitEntity.DiscountEntity
    ) = when {
        a.percent!! - b.percent!! > 0.0 -> 1
        a.percent!! - b.percent!! < 0.0 -> -1
        else -> 0
    }
}