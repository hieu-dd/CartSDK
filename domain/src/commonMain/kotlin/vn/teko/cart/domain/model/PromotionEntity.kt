package vn.teko.cart.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import vn.teko.cart.domain.util.RFC3339DateTimeSerializer
import vn.teko.cart.domain.util.PlatformDateFromTimeStampSerializer


@Serializable
data class PromotionEntity(
    var id: Int = 0,

    var isDefault: Boolean = false,

    var condition: ConditionEntity = ConditionEntity(),

    var benefit: BenefitEntity = BenefitEntity(),

    var applyOn: String,

    var name: String = "",

    var description: String = "",

    @Serializable(with = RFC3339DateTimeSerializer::class)
    var startedAt: Instant? = null,

    @Serializable(with = RFC3339DateTimeSerializer::class)
    var endedAt: Instant? = null,

    var isPrivate: Boolean = false,

    var timeRanges: List<TimeRangeEntity> = listOf(),

    var applySellerIds: List<Int> = listOf(),
) {

    @Serializable(with = PlatformDateFromTimeStampSerializer::class)
    val endTimestampSec: Instant?

    init {
        endTimestampSec = endedAt
    }


    @Serializable
    data class ConditionEntity(
        var coupon: String? = null,

        var orderValueMax: Double? = null,

        var orderValueMin: Double? = null,

        var blockSize: Int = 1,

        var minQuantity: Int = 1,

        var timeRanges: List<TimeRangeEntity> = listOf(),

        var exclusions: List<ExclusionEntity> = listOf(),

        var paymentMethods: List<String> = listOf(),

        var skus: MutableList<SkuEntity> = mutableListOf(),
    )

    @Serializable
    data class ExclusionEntity(
        val applyOn: List<String>,

        val isDefault: List<Boolean>
    )

    @Serializable
    data class SkuEntity(
        var sku: String = ""
    )


    @Serializable
    data class TimeRangeEntity(
        val start: String,

        val end: String
    )

    @Serializable
    data class BenefitEntity(
        var discount: DiscountEntity? = null,

        var voucher: VoucherEntity? = null,

        var gifts: List<GiftEntity>? = null
    ) {
        @Serializable
        data class VoucherEntity(
            var name: String = "",

            var quantity: Int = 0,

            var maxQuantity: Int?,

            override val budgetStatus: String?,

            override val outOfBudget: Boolean?
        ) : WithBudget

        @Serializable
        data class DiscountEntity(
            var percent: Double? = null,

            var maxAmount: Double? = null,

            var flat: Double? = null,

            var maxAmountPerOrder: Long? = null,

            override val budgetStatus: String?,

            override val outOfBudget: Boolean?
        ) : WithBudget

        @Serializable
        data class GiftEntity(
            var sku: String = "",

            var name: String = "",

            var imageUrl: String = "",

            var quantity: Int = 0,

            var maxQuantityPerOrder: Int? = null,

            override val budgetStatus: String?,

            override val outOfBudget: Boolean?
        ) : WithBudget
    }

}
