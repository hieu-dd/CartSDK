package vn.teko.cart.domain.extension

import vn.teko.cart.domain.config.ApplyPromotionConfig
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.ProductEntity
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.util.PlatformDateBridge
import vn.teko.cart.domain.util.randomUUIDString

/**
 * Check if product has price
 */
fun ProductEntity.isValid(): Boolean {
    return prices.isNotEmpty() && status.sellable
}


/**
 * Get the based price of product
 */
internal fun ProductEntity.getPrice(): Long {
    if (prices.isNullOrEmpty()) return 0L
    return prices[0].sellPrice
}

/**
 * Generate orderline item from product
 */
internal fun ProductEntity.generateLineItem(quantity: Int, lineItemId: String?, consultationServiceFee: Double? = null): CartItemEntity {
    return CartItemEntity(
        lineItemId = lineItemId ?: randomUUIDString(),
        sku = productInfo.sku,
        name = productInfo.name,
        displayName = productInfo.name,
        vatRate = productInfo.tax?.taxOut ?: 10,
        quantity = quantity,
        unitPrice = getPrice(),
        sellerId = productInfo.seller.id,
        product = this,
        addedAt = PlatformDateBridge.getCurrentDate(),
        isShippingFeeItem = false,
        consultationServiceFee = consultationServiceFee ?: 0.0
    )
}

/**
 * Get all valid promotions from [product][ProductEntity]
 */
internal fun ProductEntity.getValidPromotions(
    productQuantity: Int,
    ignoreCheckQuantity: Boolean = false,
    applyPromotionConfig: ApplyPromotionConfig,
    exclusions: List<PromotionEntity.ExclusionEntity> = listOf()
): List<PromotionEntity> {
    return promotions
        .filter { definition ->
            definition.isValidForProduct(
                productInfo.seller.id, productQuantity, ignoreCheckQuantity, applyPromotionConfig
            ) && !definition.isContainsInExclusions(exclusions)
        }
}

fun ProductEntity.attachSellerIdToPromotion() {
    promotions.forEach {
        it.applySellerIds = listOf(productInfo.seller.id)
    }
}