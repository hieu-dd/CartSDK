package vn.teko.cart.core.infrastructure.cart.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateItemsBySellerRequest(
    val selected: Boolean?
)