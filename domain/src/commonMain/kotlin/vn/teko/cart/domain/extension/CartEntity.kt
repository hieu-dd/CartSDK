package vn.teko.cart.domain.extension

import vn.teko.cart.domain.config.ApplyPromotionConfig
import vn.teko.cart.domain.constants.APPLY_TYPE_ORDER
import vn.teko.cart.domain.constants.APPLY_TYPE_PART_ORDER
import vn.teko.cart.domain.model.*
import vn.teko.cart.domain.util.PlatformDateBridge
import vn.teko.cart.domain.util.PromotionExtractor.getApplicableFlashSale
import vn.teko.cart.domain.util.PromotionExtractor.getApplicablePromotions
import kotlin.math.roundToLong

/**
 * get current coupon of cart with raw data
 */
internal fun CartEntity.getCurrentCoupon(): String? {
    return promotions.find { it.isCoupon() }?.coupon
}

internal fun CartEntity.getSelectedPromotionAppliedOnItemFromRawData(
    item: CartItemEntity
): CartPromotionEntity? {
    return getPromotionsAppliedOnItem(item, true).firstOrNull { appliedPromotion ->
        appliedPromotion.coupon.isNullOrEmpty()
    }
}

internal fun CartEntity.getSelectedPromotionAppliedOnItem(
    item: CartItemEntity
): CartPromotionEntity? {
    return getPromotionsAppliedOnItem(item, true).firstOrNull { appliedPromotion ->
        with(appliedPromotion.base) {
            this != null && !this.isDefault && !this.isCouponPromotionForProduct()
        }
    }
}

internal fun CartEntity.getPromotionsAppliedOnItem(
    item: CartItemEntity,
    includedFlashSale: Boolean
): List<CartPromotionEntity> {
    return getPromotionsAppliedOnItem(item)
        .filter { includedFlashSale || !it.isFlashSale() }
}

internal fun CartEntity.getPromotionsAppliedOnItem(item: CartItemEntity): List<CartPromotionEntity> {
    return promotions.filter { it.isAppliedOnItem(item) }
}

internal fun CartEntity.removeProductItem(item: CartItemEntity) {
    items.remove(item)
    promotions.filter { it.isAppliedOnItem(item) }
        .forEach { promotions.remove(it) }
}

// Find match item in order
internal fun CartEntity.findMatchProductItem(
    sku: String,
    selectedPromotionId: Int?
): List<CartItemEntity> {
    val matchedItems = mutableListOf<CartItemEntity>()
    items.forEach { existentProductItem ->
        val isSameProductButDifferentItem = existentProductItem.sku == sku
        if (isSameProductButDifferentItem) {
            val appliedPromotions = getAppliedAndExpectedPromotionIdsForItem(existentProductItem, true)
            if ((selectedPromotionId != null && appliedPromotions.contains(selectedPromotionId)) ||
                (selectedPromotionId == null && appliedPromotions.isEmpty())
            ) {
                matchedItems.add(existentProductItem)
            }
        }
    }
    return matchedItems
}

internal fun CartEntity.getAppliedAndExpectedPromotionIdsForItem(
    item: CartItemEntity,
    includedFlashSale: Boolean
): List<Int> {
    val appliedPromotions = getPromotionsAppliedOnItem(item, includedFlashSale)
    val promotionIds = appliedPromotions.mapById()
        .toMutableList()
    val expectedPromotionId = item.expectedPromotion?.id
    if (expectedPromotionId != null && item.expectedPromotion?.condition?.blockSize ?: 0 > 1) {
        promotionIds.add(expectedPromotionId)
    }

    return promotionIds.distinct()
        .sorted()
}


internal fun CartEntity.addItem(
    product: ProductEntity,
    quantity: Int,
    selectedPromotion: PromotionEntity?,
    couponPromotion: PromotionEntity?,
    expectedId: String?,
    consultationServiceFee: Double? = null
): CartItemEntity {
    val item = product.generateLineItem(quantity, id, consultationServiceFee).apply {
        isNew = true
        if (!expectedId.isNullOrEmpty()) {
            id = expectedId
            lineItemId = id
        }
    }

    val blockSize = selectedPromotion?.getBlockSize() ?: 1
    if (blockSize > 1) {
        item.quantity = blockSize
    }

    items.add(item)
    val exclusions =
        getCouponPromotionForSeller(product.productInfo.seller.id)?.condition?.exclusions ?: listOf()
    return applyPromotionForProductItem(item, selectedPromotion, couponPromotion, exclusions)
}

internal fun CartEntity.getCouponPromotionForSeller(sellerId: Int): PromotionEntity? {
    return promotions.firstOrNull {
        it.isPartOrderCoupon(sellerId) || it.isOrderCoupon(sellerId)
    }?.base
}

internal fun CartEntity.applyPromotionForProductItem(
    productItem: CartItemEntity,
    selectedPromotion: PromotionEntity?,
    selectedCoupon: PromotionEntity?,
    exclusions: List<PromotionEntity.ExclusionEntity> = listOf()
): CartItemEntity {
    productItem.updateExpectedPromotion(selectedPromotion)
    productItem.updateExpectedCouponPromotion(selectedCoupon)
    // remove all invalid promotions
    invalidateItemPromotions(productItem, selectedPromotion)

    // all promotions that are applied on items after removed all invalid promotions
    val appliedPromotions = getPromotionsAppliedOnItem(productItem, false).map { it.base!! }
    val validPromotions = productItem.product!!.getValidPromotions(
        productItem.quantity, applyPromotionConfig = ApplyPromotionConfig(),
        exclusions = exclusions
    )
    // all promotions that can be applied on item
    val applicablePromotions = getApplicablePromotions(
        productItem, appliedPromotions, validPromotions, selectedPromotion,
        true, selectedCoupon, exclusions
    )
    applicablePromotions.forEach {
        applyPromotionForItem(productItem, it.generateOrderPromotionForItem(productItem))
    }

    getApplicableFlashSale(productItem, applicablePromotions)?.let {
        applyPromotionForItem(productItem, it.generateFlashSalePromotion(productItem))
    }
    // Get match item that has same sku and same promotions applied on it
    return matchingProductItem(productItem, exclusions).apply {
        calculatePrices(getPromotionsAppliedOnItem(this))
        finalizeAction(sellerId)
    }
}

internal fun CartEntity.matchingProductItem(
    productItem: CartItemEntity,
    exclusions: List<PromotionEntity.ExclusionEntity>
): CartItemEntity {
    val matchedItems = findMatchedProductItem(productItem)
    val quantityAfterUpdate = productItem.quantity + matchedItems.sumBy { it.quantity }
    val finalItem = if (matchedItems.isNotEmpty()) matchedItems[0] else productItem
    if (matchedItems.isNotEmpty()) {
        matchedItems.forEach { matchedProductItem ->
            if (matchedProductItem.lineItemId != finalItem.lineItemId) {
                removeProductItem(matchedProductItem)
            }
        }
        removeProductItem(productItem)
        updateProductItem(finalItem, quantityAfterUpdate, exclusions)
    }
    finalItem.addedAt = PlatformDateBridge.getCurrentDate()

    return finalItem
}

internal fun CartEntity.updateProductItem(
    productItem: CartItemEntity,
    itemQuantity: Int,
    exclusions: List<PromotionEntity.ExclusionEntity>
): CartItemEntity {
    // If exclusions is not null then apply base on exclusions else based on current coupon exclusions
    val selectedPromotion =
        getSelectedPromotionAppliedOnItem(productItem)?.base ?: productItem.expectedPromotion

    val lastCoupon = getPromotionsAppliedOnItem(productItem, false).map { it.base!! }
        .firstOrNull { it.isCouponPromotion() }
    val selectedCoupon = getCouponToApply(productItem, lastCoupon)
    productItem.apply { quantity = itemQuantity }
    removePromotions(getPromotionsAppliedOnItem(productItem))
    return applyPromotionForProductItem(productItem, selectedPromotion, selectedCoupon, exclusions)
}

internal fun CartEntity.removePromotions(
    promotions: List<CartPromotionEntity>
) {
    promotions.forEach { removePromotion(it) }
    val updatedSellers = promotions.flatMap { it.sellerIds }.distinct()
    // Recalculate price for effected items
    updatedSellers.forEach { recalculateOrderInfoForSeller(it) }
}

internal fun CartEntity.removeGiftItems(promotion: CartPromotionEntity) {
    val giftItemIds = promotion.gifts.map { it.lineItemId }
    val giftItems = items.filter { giftItemIds.contains(it.lineItemId) }
    items.removeAll(giftItems)
}

internal fun CartEntity.removePromotion(promotion: CartPromotionEntity) {
    removeGiftItems(promotion)
    promotions.remove(promotion)
}

internal fun CartEntity.getCouponToApply(
    cartLineItem: CartItemEntity,
    lastSelectedCoupon: PromotionEntity?
): PromotionEntity? {
    return if (ApplyPromotionConfig().autoApplyCoupon) {
        getPromotionsAppliedOnItem(cartLineItem, false).map { it.base!! }
            .firstOrNull { it.isCouponPromotion() }
    } else {
        val couponToApply = lastSelectedCoupon ?: cartLineItem.expectedCouponPromotion
        if (isCouponValidForItem(couponToApply, cartLineItem)) couponToApply else null
    }
}

internal fun CartEntity.isCouponValidForItem(
    coupon: PromotionEntity?,
    cartLineItem: CartItemEntity
): Boolean {
    return coupon?.isValidForProduct(
        cartLineItem.sellerId, cartLineItem.quantity, false,
        ApplyPromotionConfig()
    ) == true
}


internal fun CartEntity.findMatchedProductItem(productItem: CartItemEntity): List<CartItemEntity> {
    val matchedItems = mutableListOf<CartItemEntity>()
    val promotionIds = getAppliedAndExpectedPromotionIdsForItem(productItem, true)
    items
        .forEach { existentProductItem ->
            val isSameProductButDifferentItem = existentProductItem.hasProduct()
                    && existentProductItem.sku == productItem.sku
                    && existentProductItem.lineItemId != productItem.lineItemId

            if (isSameProductButDifferentItem) {
                if (promotionIds == getAppliedAndExpectedPromotionIdsForItem(existentProductItem, true)) {
                    matchedItems.add(existentProductItem)
                }
            }
        }
    return matchedItems
}

internal fun CartEntity.applyPromotionForItem(
    item: CartItemEntity,
    promotion: CartPromotionEntity
) {
    addPromotion(promotion)
    item.calculatePrices(getPromotionsAppliedOnItem(item, true))
}


internal fun CartEntity.addPromotion(promotion: CartPromotionEntity) {
    promotions.add(promotion)
    addGiftItems(promotion)
}

internal fun CartEntity.addGiftItems(promotion: CartPromotionEntity) {
    items.addAll(promotion.generateGiftItems())
}

internal fun CartEntity.updateItem(
    itemId: String,
    itemQuantity: Int
): CartItemEntity {
    val item = getItem(itemId)!!
    val exclusions = getAllSelectedPromotions()
        .firstOrNull {
            it.isOrderCoupon(item.sellerId) || it.isAppliedOnPartOrder(
                item.sellerId
            )
        }?.base?.condition?.exclusions ?: listOf()
    return updateProductItem(item, itemQuantity, exclusions)
}

internal fun CartEntity.getAllSelectedPromotions(): List<CartPromotionEntity> {
    return promotions.filter { it.isSelected }
}

internal fun CartEntity.getItem(itemId: String): CartItemEntity? {
    return items.firstOrNull { it.lineItemId == itemId }
}

internal fun CartEntity.toggleItem(
    itemId: String,
    selected: Boolean
): CartItemEntity {
    val item = getItem(itemId)!!
    return toggleProductItem(item, selected)
}

internal fun CartEntity.toggleProductItem(
    productItem: CartItemEntity,
    selected: Boolean
): CartItemEntity {
    toggleItemAndPromotions(productItem, selected)
    finalizeAction(productItem.sellerId)
    return productItem
}

internal fun CartEntity.toggleItemAndPromotions(
    item: CartItemEntity,
    selected: Boolean
) {
    item.isSelected = selected
    promotions.toList().forEach {
        if (it.isAppliedOnItem(item)) togglePromotion(it, selected)
    }
}

internal fun CartEntity.togglePromotion(
    promotion: CartPromotionEntity,
    selected: Boolean
) {
    promotion.isSelected = selected
    promotion.gifts.forEach { gift ->
        getItem(gift.lineItemId)?.let { giftItem -> toggleItem(giftItem.lineItemId, selected) }
    }
}


internal fun CartEntity.buildCartData(products: List<ProductEntity>): List<CartData> {
    return items.mapNotNull { data ->
        val newProduct = products.firstOrNull { product -> product.productInfo.sku == data.sku }
        val selectedPromotionId =
            getSelectedPromotionAppliedOnItemFromRawData(data)?.promotionId ?: data.expectedPromotion?.id
        newProduct?.let {
            CartData(
                product = it,
                selectablePromotionId = selectedPromotionId,
                quantity = data.quantity,
                selected = data.isSelected,
                addedAt = data.addedAt,
                couponPromotionId = null,
                id = data.id,
                consultationServiceFee = data.consultationServiceFee
            )
        }
    }
}

internal fun CartEntity.getAllSellerIds(): Set<Int> =
    items.mapTo(mutableSetOf()) { it.sellerId }

internal fun CartEntity.getSelectedSellerIds(): Set<Int> =
    items.mapNotNullTo(mutableSetOf()) { if (it.isSelected) it.sellerId else null }

internal fun CartEntity.applyCouponPromotion(couponPromotion: PromotionEntity) {
    if (checkValidPromotion(couponPromotion)) {
        val sellerIds = couponPromotion.applySellerIds
        // Remove all product promotion with exclusion
        removePromotions(promotions.filter {
            (sellerIds.isEmpty() || it.sellerIds.intersect(couponPromotion.applySellerIds).isNotEmpty())
                    && it.base != null
                    && it.base!!.isContainsInExclusions(couponPromotion.condition.exclusions)
        })
        val (grandTotal) = calculateInvoiceWithExclusion(sellerIds, couponPromotion.condition.exclusions)
        when {
            couponPromotion.isApplyOnPartOrder() -> {
                val validOrderLineItems = getValidItemsForPartOrderPromotion(couponPromotion)
                addPromotion(
                    couponPromotion.generateOrderPromotionForPartOrder(validOrderLineItems)
                )
            }
            couponPromotion.isAppliedOnOrder() -> {
                addPromotion(
                    couponPromotion.generateOrderPromotionForOrder(grandTotal)
                )
            }
        }
        (sellerIds.ifEmpty { getAllSellerIds() }).forEach { recalculateOrderInfoForSeller(it) }
    }
}

internal fun CartEntity.addService(sellerId: Int) {
    service.add(OrderServiceEntity(sellerId = sellerId, delivery = true))
}

internal fun CartEntity.removeService(sellerId: Int) {
    service.removeAll { it.sellerId == sellerId }
}

/**
 * @param sellerIds: list of sellerId that want to collect item
 *
 * @return list of selected items  of seller if sellerIds is not empty else it will return all selected items
 *
 */
internal fun CartEntity.collectProductItemSkus(sellerIds: List<Int>): List<String> {
    val skus = mutableSetOf<String>()
    items.forEach { item ->
        if (item.hasProduct() && item.isSelected && (sellerIds.isEmpty() || sellerIds.contains(item.sellerId))) {
            skus.add(item.sku)
        }
    }
    return skus.toList()
}

// We don't care about applySellerIds of order promotion. Just apply for all seller in order
// after apply coupon for order
internal fun CartEntity.applyBestOrderPromotions(orderPromotions: List<PromotionEntity>) {
    val (grandTotal) = calculateInvoice()
    val sellerItemSkus = collectProductItemSkus(listOf())
    val applyPromotionConfig = ApplyPromotionConfig()
    orderPromotions.getPromotionsForOrder()
        .filter {
            it.isValidForOrder(
                it.applySellerIds, grandTotal, sellerItemSkus, applyPromotionConfig
            )
        }
        .maxByOrNull { it.calculateDiscount(grandTotal) }
        ?.let {
            addPromotion(it.generateOrderPromotionForOrder(grandTotal))
        }
    calculateOrderInvoice()
}

/**
 * Calculate the current invoice
 *
 * @param sellerIds id of the seller
 *
 * If the [sellerIds] is not nul, this will calculate the invoice for only that [sellerId]
 * If the [sellerIds] is null, this will calculate for all sellers (order)
 */
fun CartEntity.calculateInvoice(sellerIds: List<Int>? = null): Pair<Long, Long> {
    var totalDiscount = 0L
    var grandTotal = 0L
    items.forEach { item ->
        if (item.isSelected && item.hasProduct() && (sellerIds == null || sellerIds.contains(item.sellerId))) {
            grandTotal += (item.unitPrice + item.consultationServiceFee.toLong()) * item.quantity
        }
    }
    promotions.forEach { promotion ->
        if (promotion.isSelected && (sellerIds == null || promotion.sellerIds.intersect(sellerIds).isNotEmpty())) {
            totalDiscount += promotion.discount.roundToLong()
        }
    }
    grandTotal -= totalDiscount
    return Pair(grandTotal, totalDiscount)
}

/**
 * Calculate the current invoice
 *
 * @param sellerIds id of the seller
 *
 * If the [sellerIds] is not nul, this will calculate the invoice for only that [sellerId]
 * If the [sellerIds] is null, this will calculate for all sellers (order)
 */
internal fun CartEntity.calculateInvoiceWithExclusion(
    sellerIds: List<Int>,
    exclusions: List<PromotionEntity.ExclusionEntity>
): Pair<Long, Long> {
    var totalDiscount = 0L
    var grandTotal = 0L
    items.forEach { item ->
        if (item.isSelected && item.hasProduct() && (sellerIds.isEmpty() || sellerIds.contains(item.sellerId))) {
            grandTotal += (item.unitPrice + item.consultationServiceFee.toLong()) * item.quantity
        }
    }
    promotions.forEach { promotion ->
        if ((promotion.baseFlashSale != null
                    || (promotion.base != null && !promotion.base!!.isContainsInExclusions(exclusions)))
            && promotion.isSelected && (sellerIds.isEmpty() || promotion.sellerIds.intersect(sellerIds).isNotEmpty())
        ) {
            totalDiscount += promotion.discount.roundToLong()
        }
    }
    grandTotal -= totalDiscount
    return Pair(grandTotal, totalDiscount)
}

//TODO: check this logic with exclusion
internal fun CartEntity.checkValidPromotion(orderPromotion: PromotionEntity): Boolean {
    val sellerIds = orderPromotion.applySellerIds
    val exclusions = orderPromotion.condition.exclusions
    val (grandTotal) = calculateInvoiceWithExclusion(sellerIds, exclusions)
    val sellerItemSkus = collectProductItemSkus(sellerIds)
    val applyPromotionConfig = ApplyPromotionConfig()
    return when {
        orderPromotion.isApplyOnPartOrder() -> {
            val validOrderLineItems = getValidItemsForPartOrderPromotion(orderPromotion)
            orderPromotion.isValidForPartOrder(sellerIds, validOrderLineItems, applyPromotionConfig)
        }
        orderPromotion.isAppliedOnOrder() -> orderPromotion.isValidForOrder(
            sellerIds,
            grandTotal,
            sellerItemSkus,
            applyPromotionConfig
        )
        else -> false
    }
}

internal fun CartEntity.getValidItemsForPartOrderPromotion(orderCoupon: PromotionEntity?): List<CartItemEntity> {
    return getSelectedItems()
        .filter { lineItem ->
            !lineItem.isGift && !lineItem.isServiceFee()
                    && orderCoupon?.condition?.skus?.any { it.sku == lineItem.sku } == true
                    && orderCoupon.applySellerIds.contains(lineItem.sellerId)
        }
}

internal fun CartEntity.getSelectedItems(): List<CartItemEntity> {
    return items.filter { it.isSelected && it.hasProduct() }
}

internal fun CartEntity.getAllSelectedItems(): List<CartItemEntity> {
    return items.filter { it.isSelected }
}

internal fun CartEntity.recalculateOrderInfoForSeller(sellerId: Int) {
    items.forEach { if (sellerId == it.sellerId) it.calculatePrices(getPromotionsAppliedOnItem(it)) }
    calculateOrderInvoice()
}

internal fun CartEntity.calculateOrderInvoice() {
    val (grandTotal, totalDiscount) = calculateInvoice()
    //TODO: update real service fee
    val totalServiceFee = 0
    this.subTotal = items.filter { it.isSelected && it.hasProduct() }.sumOf { it.quantity * it.unitPrice }
    this.grandTotal = grandTotal + totalServiceFee
    this.totalDiscount = totalDiscount
}

internal fun CartEntity.getService(sellerId: Int): OrderServiceEntity? {
    return service.firstOrNull { it.sellerId == sellerId }
}

// Recalculate order info
internal fun CartEntity.finalizeAction(sellerId: Int) {
    if (getSelectedItemsOfSeller(sellerId).isEmpty()) {
        removeCouponAndPromotionForOrder(sellerId)
        removeService(sellerId)
    } else {
        getService(sellerId) ?: addService(sellerId)
    }
    calculateOrderInvoice()
}

internal fun CartEntity.getSelectedItemsOfSeller(sellerId: Int): List<CartItemEntity> {
    return items.filter { it.isSelected && it.hasProduct() && it.belongTo(sellerId) }
}

internal fun CartEntity.removeCouponAndPromotionForOrder(sellerId: Int) {
    promotions.removeAll {
        it.sellerIds.contains(sellerId)
                && (it.applyType == APPLY_TYPE_PART_ORDER || it.applyType == APPLY_TYPE_ORDER)
    }
}

/**
 * Remove all invalid promotions for a line item and all gift items which are generated from promotions
 * exclude flash sale
 *
 * @param item a product line item
 * @param itemPromotion a promotion that will be applied for item[item]
 *
 * if [itemPromotion] is null, this method will consider all [item] promotions (included gift line items) are invalid
 */
internal fun CartEntity.invalidateItemPromotions(
    item: CartItemEntity,
    itemPromotion: PromotionEntity?
) {
    // 1st step: remove all invalid promotions from order for item
    invalidatePromotionFromOrder(itemPromotion) { it.isAppliedOnItem(item) }
    // 2nd step: remove all coupons from order for item, later we will calculate it again
    val allItemCoupons = getPromotionsAppliedOnItem(item)
        .filter { it.isCouponPromotion() }
    removePromotions(allItemCoupons)
    // 3rd step: calculate the item prices

    val appliedPromotions = promotions.filter { it.isAppliedOnItem(item) }

    item.calculatePrices(appliedPromotions)
}

internal fun CartEntity.invalidatePromotionFromOrder(
    promotion: PromotionEntity?,
    promotionType: (CartPromotionEntity) -> Boolean
) {
    promotions
        .filter {
            val basePromotion = it.base
            promotionType(it)
                    && basePromotion != null
                    && (promotion == null || !promotion.canBeAppliedTogether(basePromotion))
        }
        .forEach { invalidPromotion ->
            removePromotion(invalidPromotion)
        }
}

/**
 * Start logic for order coupon
 */

internal fun CartEntity.getAllValidCoupons(promotions: List<PromotionEntity>): List<PromotionEntity> {
    val selectedSellerIds = getSelectedItems()
        .map { it.sellerId }
        .distinct()

    if (selectedSellerIds.isEmpty()) return listOf()
    return promotions.filter {
        it.isValid(applyPromotionConfig = ApplyPromotionConfig())
                && (it.applySellerIds.isEmpty() || it.applySellerIds.intersect(selectedSellerIds).isNotEmpty())
                && it.isCouponPromotion()
    }
}

internal fun CartEntity.calculateItemsValue(sellerIds: List<Int>): Long {
    return items.sumOf {
        if (it.isSelected && it.hasProduct() && (sellerIds.isEmpty() || sellerIds.contains(it.sellerId))) {
            it.rowTotal
        } else {
            0
        }
    }

}

internal fun CartEntity.isValidPartOrderPromotion(
    orderPromotion: PromotionEntity,
    sellerIds: List<Int>,
    applyPromotionConfig: ApplyPromotionConfig = ApplyPromotionConfig()
): Boolean {
    val validOrderLineItems = getValidItemsForPartOrderPromotion(orderPromotion)
    return orderPromotion.isApplyOnPartOrder() && orderPromotion.isValidForPartOrder(
        sellerIds, validOrderLineItems, applyPromotionConfig
    )
}

internal fun CartEntity.isValidOrderPromotion(
    orderPromotion: PromotionEntity,
    sellerIds: List<Int>,
    applyPromotionConfig: ApplyPromotionConfig = ApplyPromotionConfig()
): Boolean {
    val itemsRowTotal = calculateItemsValue(sellerIds)
    val sellerItemSkus = collectProductItemSkus(sellerIds)
    return orderPromotion.isAppliedOnOrder() && orderPromotion.isValidForOrder(
        sellerIds, itemsRowTotal, sellerItemSkus, applyPromotionConfig
    )
}

internal fun CartEntity.getCouponsList(couponPromotions: List<PromotionEntity>): OrderCouponsEntity {
    val applicableCoupons = mutableListOf<PromotionEntity>()
    val inapplicableCoupons = mutableListOf<PromotionEntity>()

    val grandTotal = mutableMapOf<List<Int>, Long>()

    // group by coupons from product and ppm without duplicate
    val validCoupons = getAllValidCoupons(couponPromotions).distinctBy { it.id }

    // validate coupon is applicable or inapplicable
    applicableCoupons.addAll(validCoupons.filter { it.condition.exclusions.isNotEmpty() })
    validCoupons.filter { it.condition.exclusions.isEmpty() }
        .forEach { coupon ->
            grandTotal.getOrPut(coupon.applySellerIds.sorted()) {
                calculateInvoice(coupon.applySellerIds).first
            }
            when {
                isValidPartOrderPromotion(coupon, coupon.applySellerIds) -> applicableCoupons.add(coupon)
                isValidOrderPromotion(coupon, coupon.applySellerIds) -> applicableCoupons.add(coupon)
                else -> inapplicableCoupons.add(coupon)
            }
        }
    // sort applicable coupon list with discount
    applicableCoupons.sortWith(
        compareByDescending<PromotionEntity> {
            it.calculateDiscount(grandTotal[it.applySellerIds.sorted()] ?: 0L)
        }.thenByCreated()
    )

    return OrderCouponsEntity(applicableCoupons, inapplicableCoupons)
}

internal fun CartEntity.updateOrderCoupons(couponPromotions: List<PromotionEntity>) {
    orderCoupons = getCouponsList(couponPromotions)
}
/**
 * End logic for order coupon
 */