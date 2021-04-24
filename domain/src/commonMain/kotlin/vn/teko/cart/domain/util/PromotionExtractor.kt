package vn.teko.cart.domain.util

import vn.teko.cart.domain.comparators.PromotionComparator
import vn.teko.cart.domain.extension.*
import vn.teko.cart.domain.model.FlashSaleEntity
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.ProductEntity
import vn.teko.cart.domain.model.PromotionEntity
import kotlin.jvm.JvmStatic

/**
 * Object contains methods for extract promotions which could be used from client
 *
 * On iOS/Swift, should create an instance of [PromotionExtractor] to use its methods.
 * Recommend to make an shared property for that like below:
 * ```
 *  extension PromotionExtractor {
 *    static let shared = PromotionExtractor()
 *  }
 * ```
 */
object PromotionExtractor {

    /**
     * Extracts all promotion definitions corresponding for product corresponding to the channel and terminal,
     * include invalid definitions.
     *
     * @param product a [product][ProductEntity]
     * @return list of [definition][PromotionEntity]
     *
     * example:
     * ```
     *  var allPromotions = PromotionExtractor.getAllDefinitionsForProduct(product, "vnshop", "app))
     *
     * ```
     * or with shared property on iOS/Swift
     * ```
     * let allPromotions = PromotionExtractor.getAllDefinitionsForProduct(product: product, channel: "vnshop", terminal: "app")
     * ```
     */
    @JvmStatic
    fun getAllDefinitionsForProduct(
        product: ProductEntity
    ): List<PromotionEntity> = product.promotions
        .filter { it.isAppliedOnProduct() }

    /**
     * Get all applicable promotions for [item][CartItemEntity]
     *
     * @param item [CartItemEntity] need to apply promotion
     * @param appliedPromotions promotions have been applied on [item]
     * @param validPromotions list promotions can be be applied on [item]
     * @param selectedPromotion the [promotion][PromotionEntity] must be applied on [item]
     * @return list of promotion [PromotionEntity] include [selectedPromotion], which can be applied on [item] immediately
     */
    @JvmStatic
    internal fun getApplicablePromotions(
        item: CartItemEntity,
        appliedPromotions: List<PromotionEntity>,
        validPromotions: List<PromotionEntity>,
        selectedPromotion: PromotionEntity?,
        autoApplyCoupon: Boolean = true,
        lastAppliedCoupon: PromotionEntity?,
        exclusions: List<PromotionEntity.ExclusionEntity> = listOf()
    ): List<PromotionEntity> {
        val applicablePromotions = mutableListOf<PromotionEntity>()

        if (appliedPromotions.isEmpty()) {
            applicablePromotions.addAll(getDefaultPromotions(validPromotions))
        }
        if (autoApplyCoupon) {
            validPromotions.firstOrNull { it.isCouponPromotionForProduct() }
                ?.let {
                    applicablePromotions.add(it)
                }
        } else {
            lastAppliedCoupon?.let {
                validPromotions.finById(it.id)
                    ?.let {
                        applicablePromotions.add(lastAppliedCoupon)
                    }
            }
        }

        if (selectedPromotion != null) {
            applicablePromotions.removeAll { !it.canBeAppliedTogether(selectedPromotion) }
            if (validPromotions.finById(selectedPromotion.id) != null) {
                applicablePromotions.add(selectedPromotion)
            }
        }
        applicablePromotions.removeAll { it.isContainsInExclusions(exclusions) }
        applicablePromotions.sortWith(PromotionComparator)

        return applicablePromotions
    }

    /**
     * Get applicable flash sale for [item][CartItemEntity]
     *
     * @param item [CartItemEntity] need to check and get flash sale
     * @param applicablePromotions all promotions that will be applied for [item]
     *
     * @return a [flash sale][FlashSaleEntity] which can be applied on [item] immediately
     */
    @JvmStatic
    internal fun getApplicableFlashSale(
        item: CartItemEntity,
        applicablePromotions: List<PromotionEntity>
    ): FlashSaleEntity? {
        return if (applicablePromotions.all { it.isDefault }) item.product!!.flashSales.firstOrNull { it.isValid() } else null
    }

    @JvmStatic
    internal fun getDefaultPromotions(
        validPromotions: List<PromotionEntity>
    ): List<PromotionEntity> = validPromotions
        .filter { it.isDefault }
        .sortedWith(PromotionComparator)
}
