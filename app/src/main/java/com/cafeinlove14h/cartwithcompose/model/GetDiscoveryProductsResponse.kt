package com.cafeinlove14h.cartwithcompose.model

import kotlinx.serialization.Serializable

@Serializable
data class GetDiscoverProductsResponse(
    val products: List<Product> = listOf()
)

@Serializable
data class GetDiscoverProductsApiResponse(
    val result: GetDiscoverProductsResponse = GetDiscoverProductsResponse()
)