package vn.teko.cart.core.infrastructure.cart

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import vn.teko.cart.core.CartConfig
import vn.teko.cart.core.infrastructure.cart.error.CartError
import vn.teko.cart.core.infrastructure.cart.request.*
import vn.teko.cart.core.infrastructure.cart.response.*
import vn.teko.cart.domain.model.CartTokenEntity
import vn.teko.cart.domain.model.ShippingInfoEntity

internal class CartApi(private val client: HttpClient) {

    companion object {
        const val QUERY_KEY_TERMINAL = "terminal"
        const val QUERY_KEY_CHANNEL = "channel"
        const val QUERY_SELLER_ID = "sellerId"
        const val HEADER_KEY_CART_TOKEN = "X-Cart-Token"
        const val HEADER_KEY_TENANT = "X-Tenant-Id"
        const val ENDPOINT_ADD_TO_CART = "/api/v1/cart/add-to-cart"
        const val ENDPOINT_UPDATE_ITEM = "/api/v1/cart/order-lines"
        const val ENDPOINT_UPDATE_ITEM_BY_SELLER = "/api/v1/cart/order-items"
        const val ENDPOINT_GET_CART_COUPON = "/api/v1/cart/order-coupons"
        const val ENDPOINT_APPLY_CART_COUPON = "/api/v1/cart/apply-coupon"
        const val ENDPOINT_CLEAR_COUPON = "/api/v1/cart/clear-coupon"
        const val ENDPOINT_CART = "/api/v1/cart"
        const val ENDPOINT_ADD_SHIPPING_ADDRESS = "/api/v1/cart/shipping-address"
        const val ENDPOINT_CHECKOUT = "/api/v1/cart/checkout"


        const val ENDPOINT_IDENTITY = "/api/v1/identity"
    }

    suspend fun addItem(
        sku: String,
        quantity: Int?,
        selectPromotionId: Int?,
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null
    ) = apiResult {
        client.post<AddProductToCartResponse>("${cartConfig.baseUrl}$ENDPOINT_ADD_TO_CART") {
            applyDefaults(cartConfig, userToken, cartToken)
            body = AddProductRequest(sku, quantity, selectPromotionId)
        }
    }

    suspend fun updateItem(
        lineItemId: String,
        quantity: Int?,
        selected: Boolean?,
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null
    ) = apiResult {
        client.put<UpdateItemResponse>("${cartConfig.baseUrl}$ENDPOINT_UPDATE_ITEM/${lineItemId}") {
            applyDefaults(cartConfig, userToken, cartToken)
            body = UpdateItemRequest(selected, quantity)
        }
    }

    suspend fun updateItemBySeller(
        sellerId: Int,
        selected: Boolean,
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null,
    ) = apiResult {
        client.put<Any>("${cartConfig.baseUrl}$ENDPOINT_UPDATE_ITEM_BY_SELLER") {
            applyDefaults(cartConfig, userToken, cartToken)
            parameter(QUERY_SELLER_ID, sellerId)
            body = UpdateItemsBySellerRequest(selected)
        }
        EmptyResponse()
    }

    suspend fun deleteItem(
        lineItemId: String,
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null
    ) = apiResult {
        client.delete<EmptyResponse>("${cartConfig.baseUrl}$ENDPOINT_UPDATE_ITEM/${lineItemId}") {
            applyDefaults(cartConfig, userToken, cartToken)
        }
    }

    suspend fun getAvailablePromotions(
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null,
    ) = apiResult {
        client.get<GetCartCouponsResponse>("${cartConfig.baseUrl}$ENDPOINT_GET_CART_COUPON") {
            applyDefaults(cartConfig, userToken, cartToken)
        }
    }

    suspend fun applyPromotion(
        couponCode: String,
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null
    ) = apiResult {
        client.post<ApplyCouponResponse>("${cartConfig.baseUrl}$ENDPOINT_APPLY_CART_COUPON") {
            applyDefaults(cartConfig, userToken, cartToken)
            body = ApplyCouponRequest(couponCode)
        }
    }

    suspend fun deletePromotion(
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null
    ) = apiResult {
        client.delete<Any>("${cartConfig.baseUrl}$ENDPOINT_CLEAR_COUPON") {
            applyDefaults(cartConfig, userToken, cartToken)
        }
        EmptyResponse()
    }

    suspend fun getCart(
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null,
    ) = apiResult {
        client.get<GetCartResponse>("${cartConfig.baseUrl}$ENDPOINT_CART") {
            applyDefaults(cartConfig, userToken, cartToken)
        }
    }

    suspend fun clearCart(
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null,
    ) = apiResult {
        client.delete<Any>("${cartConfig.baseUrl}$ENDPOINT_CART") {
            applyDefaults(cartConfig, userToken, cartToken)
        }
        EmptyResponse()
    }

    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "We are will provide another way to add   address when checkout"
    )
    suspend fun addShippingAddress(
        cartConfig: CartConfig,
        shippingInfo: ShippingInfoEntity,
        cartToken: String? = null,
        userToken: String? = null
    ) = apiResult {
        client.post<AddShippingAddressResponse>("${cartConfig.baseUrl}$ENDPOINT_ADD_SHIPPING_ADDRESS") {
            applyDefaults(cartConfig, userToken, cartToken)
            body = shippingInfo
        }
    }

    suspend fun checkoutCart(
        checkoutRequest: CheckoutRequest,
        cartConfig: CartConfig,
        cartToken: String? = null,
        userToken: String? = null
    ) = apiResult {
        client.post<CheckoutResponse>("${cartConfig.baseUrl}$ENDPOINT_CHECKOUT") {
            applyDefaults(cartConfig, cartToken, userToken)
            body = checkoutRequest
        }
    }

    suspend fun generateCartToken(cartConfig: CartConfig): CartTokenEntity = apiResult {
        client.get<IdentityResponse>("${cartConfig.baseUrl}$ENDPOINT_IDENTITY") {
            applyDefaults(cartConfig)
        }
    }

    /**
     * Add general information to request like tenant, terminal, channel, userToken, cartToken
     */
    private fun HttpRequestBuilder.applyDefaults(
        config: CartConfig,
        userToken: String? = null,
        cartToken: String? = null
    ) {
        parameter(QUERY_KEY_TERMINAL, config.terminal)
        parameter(QUERY_KEY_CHANNEL, config.channel)
        headers {
            header(HEADER_KEY_TENANT, config.tenant)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            if (userToken != null) header(HttpHeaders.Authorization, getBearerToken(userToken))
            if (cartToken != null) header(HEADER_KEY_CART_TOKEN, cartToken)
        }
    }

    private fun getBearerToken(token: String) = "Bearer $token"

    private suspend fun <T> apiResult(block: suspend () -> BaseCartResponse<T>): T {
        val response = block()
        return if (response.isSuccess()) {
            response.result!!
        } else {
            throw  CartError(response.statusCode, response.error ?: "")
        }
    }
}