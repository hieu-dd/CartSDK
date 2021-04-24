package com.cafeinlove14h.cartwithcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafeinlove14h.cartwithcompose.model.Product
import com.cafeinlove14h.cartwithcompose.model.response.GetDiscoverProductsApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import vn.teko.cart.core.CartManager
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val client: HttpClient, private val cartManager: CartManager) :
    ViewModel() {

    private val productsFlow = MutableStateFlow<List<Product>>(listOf())
    val products = productsFlow.asStateFlow()

    val cart = cartManager.getCartFlow(true)

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
        selectPromotionId: Int? = null
    ) {
        viewModelScope.launch {
            cartManager.addItem(sku, quantity, selectPromotionId)
        }
    }
}