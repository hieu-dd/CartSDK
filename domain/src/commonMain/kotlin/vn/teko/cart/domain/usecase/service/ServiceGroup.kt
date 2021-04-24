package vn.teko.cart.domain.usecase.service

import kotlinx.serialization.Serializable

@Serializable
data class ServiceGroup(
    val id: Int,
    val name: String,
)