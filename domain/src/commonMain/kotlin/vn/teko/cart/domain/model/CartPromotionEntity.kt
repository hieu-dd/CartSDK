package vn.teko.cart.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CartPromotionEntity(
    @SerialName("promotionId")
    var id: String = "",
    @SerialName("id")
    var promotionId: Int = 0,
    var type: String = "",
    var applyType: String = "product",
    var applyOn: MutableList<ApplyEntity> = mutableListOf(),
    var discount: Double = 0.0,
    var gifts: List<GiftEntity> = listOf(),
    var quantity: Int = 1,
    var voucher: VoucherEntity? = null,
    var coupon: String? = null,
    var isSelected: Boolean = true,
    @SerialName("basePromotion")
    var base: PromotionEntity? = null,
    var baseFlashSale: FlashSaleEntity? = null,
    var sellerIds: List<Int> = listOf(),
) {
    @Transient
    var isNew: Boolean = true

    @Serializable
    data class ApplyEntity(
        var lineItemId: String = "",
        var quantity: Int = 1,
        var sku: String = ""
    )

    @Serializable
    data class GiftEntity(
        var lineItemId: String = "",
        var sku: String = "",
        var name: String = "",
        var quantity: Int = 1
    )

    @Serializable
    data class VoucherEntity(
        var quantity: Int = 1
    )
}