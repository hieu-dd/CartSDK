package com.cafeinlove14h.cartwithcompose.model

import kotlinx.serialization.Serializable
import vn.teko.cart.android.busmodels.Product

// Get From DiscoverSDK
@Serializable
data class GetDiscoverProductsResponse(
    val products: List<Product> = listOf()
)

// Get from API
@Serializable
data class GetDiscoverProductsApiResponse(
    val result: GetDiscoverProductsResponse = GetDiscoverProductsResponse()
)