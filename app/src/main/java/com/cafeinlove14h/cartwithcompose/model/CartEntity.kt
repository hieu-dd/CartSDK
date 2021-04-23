package com.cafeinlove14h.cartwithcompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartEntity(
    @SerialName("orderLines")
    val items: List<CartItemEntity>
)