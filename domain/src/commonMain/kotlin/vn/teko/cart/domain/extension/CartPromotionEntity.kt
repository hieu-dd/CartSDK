package vn.teko.cart.domain.extension

import vn.teko.cart.domain.constants.APPLY_TYPE_ORDER
import vn.teko.cart.domain.constants.APPLY_TYPE_PART_ORDER
import vn.teko.cart.domain.constants.APPLY_TYPE_PRODUCT
import vn.teko.cart.domain.constants.ORDER_PROMOTION_TYPE_FLASH_SALE
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.CartPromotionEntity
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.util.PlatformDateBridge

internal fun CartPromotionEntity.isFlashSale(): Boolean {
    return type == ORDER_PROMOTION_TYPE_FLASH_SALE
}


internal fun CartPromotionEntity.isAppliedOnItem(item: CartItemEntity): Boolean {
    return this.applyOn.size == 1 && this.applyOn[0].lineItemId == item.lineItemId && applyType == APPLY_TYPE_PRODUCT
}


/**
 * Extracts ids of promotions
 */
internal fun List<CartPromotionEntity>.mapById(): List<Int> {
    return this.map { it.promotionId }.sorted()
}


/**
 * Whether promotion is a coupon which is applied on order
 */
internal fun CartPromotionEntity.isPartOrderCoupon(sellerId: Int? = null): Boolean {
    return isAppliedOnPartOrder(sellerId) && coupon != null
}

/**
 * Whether promotion is applied on order corresponding with seller id
 * Ignore check seller id when sellerId is null
 */
internal fun CartPromotionEntity.isAppliedOnPartOrder(sellerId: Int? = null): Boolean {
    return this.applyType == APPLY_TYPE_PART_ORDER && (sellerId == null || this.sellerIds.contains(sellerId))
}


/**
 * Whether promotion is a coupon
 */
internal fun CartPromotionEntity.isCoupon() = coupon != null


/**
 * Whether promotion is a coupon which is applied on order
 */
internal fun CartPromotionEntity.isOrderCoupon(sellerId: Int? = null): Boolean {
    return isAppliedOnOrder(sellerId) && coupon != null
}


/**
 * Whether promotion is applied on order corresponding with seller id
 * Ignore check seller id when sellerId is null
 */
internal fun CartPromotionEntity.isAppliedOnOrder(sellerId: Int? = null): Boolean {
    return this.applyType == APPLY_TYPE_ORDER && (sellerId == null || this.sellerIds.contains(sellerId))
}


internal fun CartPromotionEntity.generateGiftItems(): List<CartItemEntity> {
    return gifts.map { gift ->
        CartItemEntity(
            id = gift.lineItemId,
            isGift = true,
            lineItemId = gift.lineItemId,
            sku = gift.sku,
            name = gift.name,
            displayName = gift.name,
            quantity = gift.quantity,
            sellerId = sellerIds.first(),
            isSelected = isSelected,
            isShippingFeeItem = false,
            addedAt = PlatformDateBridge.getCurrentDate(),
            product = null
        )
    }
}

internal fun List<PromotionEntity>.getPromotionsForOrder(sellerId: Int? = null): List<PromotionEntity> =
    this.filter { sellerId == null || it.applySellerIds.contains(sellerId) }

/**
 * Whether promotion is a coupon promotion
 */
internal fun CartPromotionEntity.isCouponPromotion(): Boolean {
    return coupon != null
}