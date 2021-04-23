package vn.teko.cart.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class OrderServiceEntity(
    internal var sellerId: Int,
    internal var installation: Boolean = false,
    internal var technicalSupport: Boolean = false,
    internal var delivery: Boolean = true
)