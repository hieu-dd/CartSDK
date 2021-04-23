package com.cafeinlove14h.cartwithcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafeinlove14h.cartwithcompose.model.CartEntity
import com.cafeinlove14h.cartwithcompose.model.Product
import com.cafeinlove14h.cartwithcompose.model.request.AddProductRequest
import com.cafeinlove14h.cartwithcompose.model.response.AddProductToCartResponse
import com.cafeinlove14h.cartwithcompose.model.response.GetCartResponse
import com.cafeinlove14h.cartwithcompose.model.response.GetDiscoverProductsApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val client: HttpClient) : ViewModel() {

    private val productsFlow = MutableStateFlow<List<Product>>(listOf())
    val products = productsFlow.asStateFlow()

    private val cartFlow = MutableStateFlow<CartEntity?>(null)
    val cart = cartFlow.asStateFlow()

    fun initProducts() {
        viewModelScope.launch {
            productsFlow.value =
                client.post<GetDiscoverProductsApiResponse>("https://discovery.stag.tekoapis.net/api/v1/search") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    body = Json.parseToJsonElement(
                        """
                            {
                              "pagination": {
                                "pageNumber": 1,
                                "itemsPerPage": 200
                              },
                            	"terminalCode": "vnshop",
                            	"filter": {
                            		"sellerIds": ["1"],
                                    "skus":[]
                            	}
                            }
                        """.trimIndent()
                    )
                }.result.products
        }
    }

    init {
        initProducts()
    }

    fun addItem(
        sku: String,
        quantity: Int? = 1,
        selectPromotionId: Int? = null,
        cartToken: String? = token,
        userToken: String? = null
    ) {
        viewModelScope.launch {
            val result = client.post<AddProductToCartResponse>("${BASE_URL}$ENDPOINT_ADD_TO_CART") {
                applyDefaults(userToken, cartToken)
                body = AddProductRequest(sku, quantity, selectPromotionId)
            }
            if (result.statusCode == 0) {
                val cartEntity = getCart()
                cartFlow.value = cartEntity
            }
        }
    }

    suspend fun getCart(
        cartToken: String? = token,
        userToken: String? = null,
    ): CartEntity? {
        val result = client.get<GetCartResponse>("${BASE_URL}$ENDPOINT_CART") {
            applyDefaults(userToken, cartToken)
        }
        if (result.statusCode == 0) {
            return result.result
        } else return null
    }


    /**
     * Add general information to request like tenant, terminal, channel, userToken, cartToken
     */
    private fun HttpRequestBuilder.applyDefaults(
        userToken: String? = null,
        cartToken: String? = null
    ) {
        parameter(QUERY_KEY_TERMINAL, "vnshop")
        parameter(QUERY_KEY_CHANNEL, "vnshop")
        headers {
            header(HEADER_KEY_TENANT, "vnshop")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            if (userToken != null) header(HttpHeaders.Authorization, getBearerToken(userToken))
            if (cartToken != null) header(HEADER_KEY_CART_TOKEN, cartToken)
        }
    }

    private fun getBearerToken(token: String) = "Bearer $token"

    companion object {
        const val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ2bnNob3AiLCJpYXQiOjE2MTkxNTE4MTcsImNpZCI6Ijg4NGNjMGFmLTBhYTMtNGNjNS04ZWRlLTc1ZmQwMDM4MTk1YSJ9.sa42o4c9Su_Yi7AsHxmMr4BbSXMl4mR-W7NJwKwvpEs"
        const val BASE_URL = "https://carts-beta.stag.tekoapis.net"
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
}