package vn.teko.cart.domain.usecase.loyalty

import vn.teko.cart.domain.model.CartEntity

/**
 * The *single source of truth* of the loyalty
 *
 * This class will only handle raw loyalty from a loyalty data source
 *
 */
interface LoyaltyDataSource {
    /**
     * Get precalculate points from loyalty service that can show in get cart
     *
     * @param[merchantCode] the merchant code
     * @param[cart] current cart
     *
     * @return available [loyalty points][LoyaltyPoints] precalculate points that can return when get cart
     *
     * @throws it return null when error call instead of throw error
     */
    suspend fun preCalculatePoints(merchantCode: String, cart: CartEntity): LoyaltyPoints?
}