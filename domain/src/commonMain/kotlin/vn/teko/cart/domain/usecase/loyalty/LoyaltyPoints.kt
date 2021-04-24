package vn.teko.cart.domain.usecase.loyalty

import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyPoints(
    val points: Long?
)