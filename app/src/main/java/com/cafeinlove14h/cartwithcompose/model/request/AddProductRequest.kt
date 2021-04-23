package com.cafeinlove14h.cartwithcompose.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AddProductRequest(
    val sku: String,
    val quantity: Int?,
    val selectPromotionId: Int?
)
