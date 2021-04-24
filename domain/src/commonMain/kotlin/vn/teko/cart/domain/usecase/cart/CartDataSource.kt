package vn.teko.cart.domain.usecase.cart

import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.model.ShippingInfoEntity


interface CartDataSource {
    // cart
    suspend fun saveCart(cartEntity: CartEntity)
    suspend fun getCart(cartId: String?, userId: String?, createIfNotExists: Boolean = false): CartEntity
    suspend fun deleteCart(cartId: String?, userId: String?)
    // item
    suspend fun deleteSelectedItems(cartId: String)
    suspend fun deleteUnselectedItems(cartId: String)
    // promotion
    suspend fun saveCouponPromotion(cartId: String, couponPromotion: PromotionEntity)
    suspend fun deleteCouponPromotion(cartId: String)
    // shipment
    suspend fun addShippingInfo(cartId: String, shippingInfo: ShippingInfoEntity): ShippingInfoEntity
    suspend fun getShippingInfo(cartId: String): ShippingInfoEntity?
}