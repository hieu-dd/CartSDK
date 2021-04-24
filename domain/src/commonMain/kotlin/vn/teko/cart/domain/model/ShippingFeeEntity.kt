package vn.teko.cart.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ShippingFeeEntity(
    val shippingFee: Long,
    var serviceCode: String
)
