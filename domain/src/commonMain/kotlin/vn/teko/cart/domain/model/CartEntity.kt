package vn.teko.cart.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import vn.teko.cart.domain.config.ApplyPromotionConfig
import vn.teko.cart.domain.extension.*

@Serializable
data class CartEntity(
    val id: String,
    @SerialName("orderLines")
    var items: MutableList<CartItemEntity>,
    var promotions: MutableList<CartPromotionEntity>,
    var orderCoupons: OrderCouponsEntity = OrderCouponsEntity(),
    var service: MutableList<OrderServiceEntity> = mutableListOf(),
    var subTotal: Long = 0,
    var grandTotal: Long = 0,
    var totalDiscount: Long = 0,
    var shippingFee: Long = 0,
    var serviceCode: String = "",
    var preCalculatedPoints: Long? = null
) {
    @Transient
    var isNew: Boolean = false

    @Transient
    var userId: String? = null

    @Transient
    var shippingLocation: String? = null

    @Transient
    var defaultDeliveryService: OrderCapturePayload.AppliedService? = null

    companion object {
        const val MIN_ITEM_QUANTITY = 1
        const val MAX_ITEM_QUANTITY = 999
        fun importFrom(
            cartId: String,
            items: List<CartData>,
            exclusions: List<PromotionEntity.ExclusionEntity> = listOf()
        ): CartEntity {
            val order = CartEntity(cartId, mutableListOf(), mutableListOf())
                .apply {
                    isNew = false
                }
            items.forEach { item ->
                val product = item.product
                val promotions = product.getValidPromotions(
                    item.quantity, true, applyPromotionConfig = ApplyPromotionConfig(),
                    exclusions = exclusions
                )
                val selectedPromotion = promotions.finById(item.selectablePromotionId)
                try {
                    val importItem = order.addItem(
                        item.product,
                        item.quantity,
                        selectedPromotion,
                        null,
                        item.id,
                        item.consultationServiceFee
                    )
                        .apply {
                            isNew = false
                        }
                    // update quantity because addItem change quantity when selectedPromotion is block size promotion
                    if (selectedPromotion?.isBlockSizePromotion() == true) {
                        // Detect if item is merged with another item by calculate importItem.quantity - selectedPromotion.getBlockSize()
                        // then combined it with original item quantity
                        val newQuantity = item.quantity + importItem.quantity - selectedPromotion.getBlockSize()
                        order.updateItem(importItem.lineItemId, newQuantity)
                    }
                    // update full info of expectedPromotion
                    importItem.expectedPromotion =
                        importItem.product?.promotions?.firstOrNull { it.id == item.selectablePromotionId }
                    importItem.addedAt = item.addedAt
                    if (!item.selected) {
                        order.toggleItem(importItem.lineItemId, item.selected)
                    }
                } catch (error: Exception) {
                    // Log
                }
            }
            return order
        }
    }

    fun getSelectedPromotion(): List<CartPromotionEntity> {
        return promotions.filter { (it.base?.isSelectionPromotionForProduct() ?: true) && it.baseFlashSale == null }
    }

    fun getCouponForOrderOrPartOrder(): CartPromotionEntity? {
        return promotions.firstOrNull { it.isOrderCoupon() || it.isPartOrderCoupon() }
    }


    fun getDisplayProducts(): List<CartItemEntity> {
        return items.filter { !it.isGift && !it.isShippingFeeItem }
    }
}