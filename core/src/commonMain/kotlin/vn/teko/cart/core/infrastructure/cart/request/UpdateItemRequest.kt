package vn.teko.cart.core.infrastructure.cart.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateItemRequest(
    val selected: Boolean?,
    val quantity: Int?
)