package vn.teko.cart.core.infrastructure.cart.request

import kotlinx.serialization.Serializable

@Serializable
data class AddProductRequest(
    val sku: String,
    val quantity: Int?,
    val selectPromotionId: Int?
)