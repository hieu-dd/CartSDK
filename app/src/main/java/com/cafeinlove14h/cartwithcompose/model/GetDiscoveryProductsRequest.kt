package vn.teko.cart.android.busmodels

import kotlinx.serialization.Serializable

@Serializable
data class GetDiscoveryProductsRequest(val skus: String)