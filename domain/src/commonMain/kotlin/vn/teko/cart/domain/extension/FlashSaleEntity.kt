package vn.teko.cart.domain.extension

import vn.teko.cart.domain.constants.APPLY_TYPE_PRODUCT
import vn.teko.cart.domain.constants.ORDER_PROMOTION_TYPE_FLASH_SALE
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.CartPromotionEntity
import vn.teko.cart.domain.model.FlashSaleEntity
import vn.teko.cart.domain.util.PlatformDateBridge
import vn.teko.cart.domain.util.isInDateRange
import vn.teko.cart.domain.util.randomUUIDString
import kotlin.math.floor

internal fun FlashSaleEntity.isValid(): Boolean {
    return isAvailable() && isTimeValid()
}

internal fun FlashSaleEntity.calculateDiscount(
    basePrice: Long,
    quantity: Int?,
    consultationServiceFee: Double
): Double {
    return if (quantity != null) floor((basePrice - consultationServiceFee.toLong()) * quantity * discountPercent / 100) else 0.0
}

internal fun FlashSaleEntity.isTimeValid(): Boolean {
    val current = PlatformDateBridge.getCurrentDate()
    return current.isInDateRange(startTimestampSec, endTimestampSec)
}

internal fun FlashSaleEntity.isAvailable(): Boolean {
    return usedCount < totalCount
}

internal fun FlashSaleEntity.generateFlashSalePromotion(item: CartItemEntity): CartPromotionEntity {
    return CartPromotionEntity(
        promotionId = id,
        id = randomUUIDString(),
        type = ORDER_PROMOTION_TYPE_FLASH_SALE,
        applyType = APPLY_TYPE_PRODUCT,
        applyOn = mutableListOf(
            CartPromotionEntity.ApplyEntity(lineItemId = item.lineItemId, quantity = item.quantity, sku = item.sku)
        ),
        discount = this.calculateDiscount(item.price, item.quantity, item.consultationServiceFee),
        base = null,
        baseFlashSale = this,
        sellerIds = listOf(item.sellerId),
        isSelected = item.isSelected,
        quantity = item.quantity
    )
}