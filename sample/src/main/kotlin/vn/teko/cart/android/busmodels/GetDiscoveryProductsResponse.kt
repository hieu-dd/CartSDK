package vn.teko.cart.android.busmodels
import kotlinx.serialization.Serializable

// Get From DiscoverSDK
@Serializable
data class GetDiscoverProductsResponse(
    val products: List<DiscoveryProduct> = listOf()
)

// Get from API
@Serializable
data class GetDiscoverProductsApiResponse(
    val result: GetDiscoverProductsResponse = GetDiscoverProductsResponse()
)