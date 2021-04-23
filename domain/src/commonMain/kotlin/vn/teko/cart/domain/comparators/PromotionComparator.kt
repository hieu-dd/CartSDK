package vn.teko.cart.domain.comparators

import vn.teko.cart.domain.constants.APPLY_TYPE_ORDER
import vn.teko.cart.domain.constants.APPLY_TYPE_PRODUCT
import vn.teko.cart.domain.extension.isCouponPromotion
import vn.teko.cart.domain.model.PromotionEntity


object PromotionComparator : Comparator<PromotionEntity> {

    override fun compare(
        a: PromotionEntity,
        b: PromotionEntity
    ): Int = when {
        a.applyOn == b.applyOn -> getComparator(a.applyOn).compare(a, b)
        else -> applyTypeToValue(a.applyOn) - applyTypeToValue(b.applyOn)
    }

    private fun getComparator(applyOn: String?) = when (applyOn) {
        APPLY_TYPE_PRODUCT -> ComparatorForTypeProduct
        else -> DefaultPromotionComparator
    }

    private fun applyTypeToValue(type: String?) = when (type) {
        APPLY_TYPE_PRODUCT -> 1
        APPLY_TYPE_ORDER -> 2
        else -> 0
    }

}

private object DefaultPromotionComparator : Comparator<PromotionEntity> {

    override fun compare(
        a: PromotionEntity,
        b: PromotionEntity
    ) = 0
}

private object ComparatorForTypeProduct : Comparator<PromotionEntity> {

    override fun compare(
        a: PromotionEntity,
        b: PromotionEntity
    ): Int {
        var valueDiff = evaluate(a) - evaluate(b)

        if (valueDiff == 0) {
            valueDiff = BenefitComparator.compare(a.benefit, b.benefit)
        }

        return valueDiff
    }

    private fun evaluate(definition: PromotionEntity): Int {
        var value = 0

        value += if (definition.isDefault) 0 else 10
        value += if (definition.isCouponPromotion()) 1 else 0

        return value
    }
}