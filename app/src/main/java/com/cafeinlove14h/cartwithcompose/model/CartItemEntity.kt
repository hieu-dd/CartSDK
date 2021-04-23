package com.cafeinlove14h.cartwithcompose.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItemEntity(
    val sku: String,
    val name: String,
    val quantity: Int
)