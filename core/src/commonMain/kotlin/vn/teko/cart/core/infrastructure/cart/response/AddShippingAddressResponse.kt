package vn.teko.cart.core.infrastructure.cart.response

import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.ShippingInfoEntity

@Serializable
data class AddShippingAddressResponse(
    override val statusCode: Int = 0,
    override val error: String? = null,
    override val result: ShippingInfoEntity? = null
): BaseCartResponse<ShippingInfoEntity>()