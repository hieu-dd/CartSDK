package com.cafeinlove14h.cartwithcompose.model.response

import com.cafeinlove14h.cartwithcompose.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class GetDiscoverProductsResponse(
    val products: List<Product> = listOf()
)

@Serializable
data class GetDiscoverProductsApiResponse(
    val result: GetDiscoverProductsResponse = GetDiscoverProductsResponse()
)