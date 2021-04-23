package com.cafeinlove14h.cartwithcompose.model.response

import com.cafeinlove14h.cartwithcompose.model.CartEntity
import kotlinx.serialization.Serializable

@Serializable
data class GetCartResponse(
    val statusCode: Int = 0,
    val error: String? = null,
    val result: CartEntity? = null
)
