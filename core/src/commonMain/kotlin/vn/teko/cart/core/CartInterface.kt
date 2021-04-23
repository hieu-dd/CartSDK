package vn.teko.cart.core

import vn.teko.cart.core.infrastructure.cart.request.CheckoutRequest
import vn.teko.cart.domain.model.*

internal interface CartInterface {
    suspend fun generateCartToken(): String

    // interact with cart item: add, update, remove
    suspend fun addItem(sku: String, quantity: Int? = null, selectPromotionId: Int? = null): CartItemEntity

    suspend fun updateItem(lineItemId: String, quantity: Int?, selected: Boolean?): CartItemEntity

    suspend fun deleteItem(lineItemId: String)

    suspend fun updateItemsBySeller(sellerId: Int, selected: Boolean)

    // interact with cart promotion
    suspend fun getAvailablePromotions(): OrderCouponsEntity

    suspend fun applyPromotion(couponCode: String): PromotionEntity

    suspend fun deletePromotion()

    // get and clear full cart
    suspend fun getCart(): CartEntity

    suspend fun clearCart(): Unit

    // start checkout flow
    suspend fun addShippingAddress(shippingInfo: ShippingInfoEntity): ShippingInfoEntity

    suspend fun checkoutCart(checkoutRequest: CheckoutRequest): OrderCaptureResponse.OrderCaptureData

}