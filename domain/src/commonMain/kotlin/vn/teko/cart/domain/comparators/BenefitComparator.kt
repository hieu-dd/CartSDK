package vn.teko.cart.domain.comparators

import vn.teko.cart.domain.model.PromotionEntity

object BenefitComparator : Comparator<PromotionEntity.BenefitEntity> {
    override fun compare(
        a: PromotionEntity.BenefitEntity,
        b: PromotionEntity.BenefitEntity
    ) = when {
        a.discount != null && b.discount != null ->
            MoneyComparator.compare(a.discount!!, b.discount!!)
        a.discount != null -> -1
        b.discount != null -> 1
        else -> 0
    }
}