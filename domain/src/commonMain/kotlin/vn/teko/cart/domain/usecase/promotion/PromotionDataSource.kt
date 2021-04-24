package vn.teko.cart.domain.usecase.promotion

import vn.teko.cart.domain.exception.ExternalServiceError.PpmError
import vn.teko.cart.domain.model.PromotionEntity

/**
 * The *single source of truth* of the promotion
 *
 * This class will only handle raw promotion from a promotion data source
 *
 */
interface PromotionDataSource {

    /**
     * Get all promotions that can be applied on current cart based on cart item skus
     *
     * @param[skus] cart item skus
     *
     * @return all available [promotions][PromotionEntity] based on item skus
     *
     * @throws [PpmError.UnspecifiedError], [PpmError.GetOrderPromotionsError]
     */
    suspend fun getOrderPromotions(skus: List<String>): List<PromotionEntity>

    /**
     * Get coupon detail
     *
     * @param[coupon] the coupon code
     * @param[selectedSkus] all selected skus
     * @param[userToken] current authorization token of user if user is logged in, default is null for guest
     *
     * @return the [coupon detail][PromotionEntity]
     *
     * @throws [PpmError.UnspecifiedError], [PpmError.GetCouponPromotionError]
     */
    suspend fun getCouponDetail(
        coupon: String,
        selectedSkus: List<String>,
        userToken: String? = null
    ): PromotionEntity

}