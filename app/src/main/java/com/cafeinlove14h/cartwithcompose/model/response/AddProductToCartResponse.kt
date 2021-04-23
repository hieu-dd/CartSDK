package com.cafeinlove14h.cartwithcompose.model.response

import com.cafeinlove14h.cartwithcompose.model.CartItemEntity
import kotlinx.serialization.Serializable

@Serializable
data class AddProductToCartResponse(
    val statusCode: Int = 0,
    val error: String? = null,
    val result: CartItemEntity? = null
)
