package vn.teko.cart.domain.extension

import vn.teko.cart.domain.config.ApplyPromotionConfig
import vn.teko.cart.domain.constants.*
import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.CartPromotionEntity
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.util.PlatformDateBridge
import vn.teko.cart.domain.util.isInDateRange
import vn.teko.cart.domain.util.isInTimeRanges
import vn.teko.cart.domain.util.randomUUIDString
import kotlin.math.min


internal fun PromotionEntity.isCouponPromotionForProduct(): Boolean {
    return getPromotionType() == PROMOTION_TYPE_COUPON && isAppliedOnProduct()
}

internal fun PromotionEntity.isAppliedOnProduct(): Boolean {
    return applyOn == APPLY_TYPE_PRODUCT
}

fun PromotionEntity.getPromotionType() = when {
    !condition.coupon.isNullOrEmpty() -> PROMOTION_TYPE_COUPON
    applyOn == APPLY_TYPE_PRODUCT -> PROMOTION_TYPE_PRODUCT
    applyOn == APPLY_TYPE_ORDER -> PROMOTION_TYPE_ORDER
    else -> ""
}

internal fun PromotionEntity.isValidForProduct(
    sellerId: Int,
    productQuantity: Int = 1,
    ignoreCheckQuantity: Boolean = false,
    applyPromotionConfig: ApplyPromotionConfig
): Boolean {
    return canBeAppliedForSellers(listOf(sellerId))
            && isAppliedOnProduct()
            && isValid(productQuantity, ignoreCheckQuantity, applyPromotionConfig)
}


/**
 * Whether a promotion[PromotionEntity] is for a seller
 */
internal fun PromotionEntity.canBeAppliedForSellers(sellerIds: List<Int>): Boolean {
    return this.applySellerIds.isEmpty() || this.applySellerIds.containsAll(sellerIds)
}

/**
 * General util for checking a [promotion][PromotionEntity] is valid or not
 * Note: this should not be used directly
 *
 * @see isValidForProduct
 */
internal fun PromotionEntity.isValid(
    productQuantity: Int = 1,
    ignoreCheckQuantity: Boolean = false,
    applyPromotionConfig: ApplyPromotionConfig = ApplyPromotionConfig()
): Boolean {
    val current = PlatformDateBridge.getCurrentDate()
    val isInDateRange = current.isInDateRange(startedAt, endedAt)
    val isInTimeRange = current.isInTimeRanges(timeRanges)
    val isBenefitValid = isValidBenefit(applyPromotionConfig)
    val isBlockSizeValid = isBlockSizeValid(productQuantity, ignoreCheckQuantity)
    val isMinQuantityValid = isMinQuantityValid()

    return isBenefitValid
            && isInDateRange
            && isInTimeRange
            && isBlockSizeValid
            && isMinQuantityValid
}

internal fun PromotionEntity.getBlockSize(): Int = condition.blockSize

internal fun PromotionEntity.getQuantityToApply(productQuantity: Int): Int =
    productQuantity.div(getBlockSize())


internal fun PromotionEntity.getAppliedOnQuantity(productQuantity: Int): Int =
    getQuantityToApply(productQuantity) * getBlockSize()

internal fun PromotionEntity.isSelectionPromotionForProduct(): Boolean =
    isProductPromotion() && isAppliedOnProduct() && !isDefault

internal fun PromotionEntity.isProductPromotion(): Boolean {
    return getPromotionType() == PROMOTION_TYPE_PRODUCT
}

internal fun PromotionEntity.isValidBlockSizeBenefit(): Boolean =
    benefit.hasOnlyGift()

internal fun PromotionEntity.isMinQuantityValid(): Boolean {
    return condition.minQuantity == 1
}

/**
 * return benefit is valid or not
 *
 * @return true if benefit is valid
 */
internal fun PromotionEntity.isValidBenefit(applyPromotionConfig: ApplyPromotionConfig): Boolean {
    return (getPromotionType() == PROMOTION_TYPE_COUPON && isValidCouponBenefit(applyPromotionConfig))
            || (getPromotionType() != PROMOTION_TYPE_COUPON && benefit.isValid())
}


/**
 * check is valid coupon benefit or not
 *
 * @return true if it's benefit has only money
 * else return false
 */
internal fun PromotionEntity.isValidCouponBenefit(applyPromotionConfig: ApplyPromotionConfig): Boolean {
    return benefit.isValid()
            && (benefit.hasOnlyMoney() || applyPromotionConfig.allowCouponWithGift)
}

internal fun PromotionEntity.isBlockSizeValid(
    productQuantity: Int = 1,
    ignoreCheckQuantity: Boolean = false
): Boolean {
    if (getBlockSize() == 1) {
        return true
    }

    if (!isSelectionPromotionForProduct()) {
        return false
    }

    if (!isValidBlockSizeBenefit()) {
        return false
    }

    return productQuantity >= getBlockSize() || ignoreCheckQuantity
}

/**
 * Whether a promotion[PromotionEntity] is valid with a list of Exclusion
 */
internal fun PromotionEntity.isContainsInExclusions(exclusion: List<PromotionEntity.ExclusionEntity>): Boolean {
    return exclusion.any {
        it.applyOn.contains(this.applyOn) && it.isDefault.contains(this.isDefault)
    }
}

/**
 * Whether a promotion [PromotionEntity] is a coupon
 */
internal fun PromotionEntity.isCouponPromotion(): Boolean {
    return getPromotionType() == PROMOTION_TYPE_COUPON
}

internal fun PromotionEntity.canBeAppliedTogether(other: PromotionEntity): Boolean {
    return when {
        this.isDefault || other.isDefault -> true
        // TODO("Probably even different types still can't be applied. We may need to check it first")
        this.getPromotionType() != other.getPromotionType() -> true
        // TODO("And event if they are having the same type, they still can be applied together")
        else -> false
    }
}

internal fun PromotionEntity.generateOrderPromotionForItem(
    item: CartItemEntity
): CartPromotionEntity {
    val quantityToApply = getQuantityToApply(item.quantity)
    val appliedOnQuantity = getAppliedOnQuantity(item.quantity)

    return CartPromotionEntity(
        id = randomUUIDString(),
        promotionId = id,
        type = ORDER_PROMOTION_TYPE_PROMOTION,
        applyType = APPLY_TYPE_PRODUCT,
        applyOn = mutableListOf(CartPromotionEntity.ApplyEntity(item.lineItemId, appliedOnQuantity, item.sku)),
        discount = calculateDiscount((item.price - item.consultationServiceFee.toLong()), item.quantity),
        gifts = generateGifts(quantityToApply),
        quantity = quantityToApply,
        coupon = condition.coupon,
        base = this,
        voucher = generateVoucher(item.quantity),
        sellerIds = listOf(item.sellerId),
        isSelected = item.isSelected
    )
}

internal fun PromotionEntity.calculateDiscount(
    basePrice: Long,
    productQuantity: Int? = null
): Double {
    benefit.discount ?: return 0.0

    val itemQuantity = productQuantity ?: 1

    var totalDiscount = 0.0
    totalDiscount += benefit.discount!!.getDiscountAsMoney(basePrice, itemQuantity)

    return totalDiscount
}

internal fun PromotionEntity.generateVoucher(quantity: Int): CartPromotionEntity.VoucherEntity? {
    return benefit.voucher?.let {
        CartPromotionEntity.VoucherEntity(
            min(
                it.quantity * quantity,
                it.maxQuantity ?: Int.MAX_VALUE
            )
        )
    }
}

internal fun PromotionEntity.generateGifts(quantity: Int): List<CartPromotionEntity.GiftEntity> {
    return benefit.gifts?.map { item ->
        CartPromotionEntity.GiftEntity(
            randomUUIDString(), item.sku, item.name,
            min(item.quantity * quantity, item.maxQuantityPerOrder ?: Int.MAX_VALUE)
        )
    } ?: listOf()
}


fun List<PromotionEntity>.finById(promotionId: Int?): PromotionEntity? =
    find { it.id == promotionId }


internal fun PromotionEntity.isBlockSizePromotion(): Boolean = getBlockSize() > 1

/**
 * Whether a promotion [PromotionEntity] is a applied on order
 */
internal fun PromotionEntity.isAppliedOnOrder(): Boolean {
    return applyOn == APPLY_TYPE_ORDER
}


/**
 * Whether [promotion][PromotionEntity] is valid for a order
 */
internal fun PromotionEntity.isValidForOrder(
    sellerIds: List<Int>,
    orderValue: Long,
    existingSkusForSeller: List<String>,
    applyPromotionConfig: ApplyPromotionConfig = ApplyPromotionConfig()
): Boolean {
    val minOrderValue = condition.orderValueMin ?: 0.0
    val maxOrderValue = condition.orderValueMax ?: Double.MAX_VALUE
    val isInValueRange = orderValue.toDouble() in minOrderValue..maxOrderValue
    val isOrderNotEmpty = existingSkusForSeller.isNotEmpty()
    val isValidSku = condition.skus.isNullOrEmpty()
            || condition.skus.any { existingSkusForSeller.contains(it.sku) }

    return isOrderNotEmpty
            && canBeAppliedForSellers(sellerIds)
            && isAppliedOnOrder()
            && isInValueRange
            && isValidSku
            && isValid(applyPromotionConfig = applyPromotionConfig)
}


/**
 * Generate an [promotion][CartPromotionEntity] item from this definition for add to [CartItemEntity]
 *
 * @param currentGrandTotal grand total of order for calculate discount of promotion
 * @return an corresponding [promotion][CartPromotionEntity] with given params
 */
internal fun PromotionEntity.generateOrderPromotionForOrder(
    currentGrandTotal: Long
): CartPromotionEntity {
    val quantity = 1

    return CartPromotionEntity(
        id = randomUUIDString(),
        promotionId = id,
        type = ORDER_PROMOTION_TYPE_PROMOTION,
        applyType = APPLY_TYPE_ORDER,
        discount = calculateDiscount(currentGrandTotal),
        gifts = generateGifts(quantity),
        quantity = quantity,
        voucher = generateVoucher(1),
        coupon = condition.coupon,
        base = this,
        sellerIds = applySellerIds
    )
}

/**
 * Whether a promotion [PromotionEntity] is a applied on  part of order
 */
internal fun PromotionEntity.isApplyOnPartOrder(): Boolean {
    return applyOn == APPLY_TYPE_PART_ORDER
}


/**
 * Generate an [promotion][CartPromotionEntity] item from this definition for items that is valid order [CartEntity]
 *
 * @param orderLineItems that is in condition of this definition
 * @return an corresponding [promotion][CartPromotionEntity] with given params
 */
internal fun PromotionEntity.generateOrderPromotionForPartOrder(orderLineItems: List<CartItemEntity>): CartPromotionEntity {
    val quantity = 1
    val itemsValue = orderLineItems.sumOf { it.rowTotal }
    return CartPromotionEntity(
        id = randomUUIDString(),
        applyOn = orderLineItems.map {
            CartPromotionEntity.ApplyEntity(
                lineItemId = it.lineItemId, quantity = it.quantity, sku = it.sku
            )
        }.toMutableList(),
        promotionId = id,
        type = ORDER_PROMOTION_TYPE_PROMOTION,
        applyType = APPLY_TYPE_PART_ORDER,
        discount = calculateDiscount(itemsValue),
        gifts = generateGifts(quantity),
        quantity = quantity,
        voucher = generateVoucher(1),
        coupon = condition.coupon,
        base = this,
        sellerIds = applySellerIds
    )
}

internal fun PromotionEntity.isValidForPartOrder(
    sellerIds: List<Int>,
    orderLineItems: List<CartItemEntity>,
    applyPromotionConfig: ApplyPromotionConfig = ApplyPromotionConfig()
): Boolean {
    val minOrderValue = condition.orderValueMin ?: 0.0
    val maxOrderValue = condition.orderValueMax ?: Double.MAX_VALUE
    val isInValueRange = orderLineItems.sumOf { it.rowTotal }.toDouble() in minOrderValue..maxOrderValue
    val isOrderNotEmpty = orderLineItems.isNotEmpty()
    val isValidSku = condition.skus.isNullOrEmpty()
            || condition.skus.any { skuEntity ->
        orderLineItems.map { orderLineItem -> orderLineItem.sku }
            .contains(skuEntity.sku)
    }

    return isOrderNotEmpty
            && canBeAppliedForSellers(sellerIds)
            && isApplyOnPartOrder()
            && isInValueRange
            && isValidSku
            && isValid(applyPromotionConfig = applyPromotionConfig)
}


/**
 * Whether a promotion[PromotionEntity] is for order but not coupon
 */
internal fun PromotionEntity.isOrderPromotion(): Boolean {
    return getPromotionType() == PROMOTION_TYPE_ORDER && isAppliedOnOrder()
}


/**
 * Start logic for get order coupon
 */
fun Comparator<PromotionEntity>.thenByCreated(): Comparator<PromotionEntity> =
    thenByDescending { it.id }

internal fun PromotionEntity.isCouponPromotionForOrder(): Boolean {
    return getPromotionType() == PROMOTION_TYPE_COUPON && isAppliedOnOrder()
}

internal fun PromotionEntity.isCouponPromotionForPartOrder(): Boolean {
    return getPromotionType() == PROMOTION_TYPE_COUPON && isApplyOnPartOrder()
}


/**
 * End logic for get order coupon
 */