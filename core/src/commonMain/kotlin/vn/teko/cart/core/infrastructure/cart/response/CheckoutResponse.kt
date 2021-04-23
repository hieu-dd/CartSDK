package vn.teko.cart.core.infrastructure.cart.response

import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.OrderCaptureResponse

@Serializable
data class CheckoutResponse(
    override val statusCode: Int = 0,
    override val error: String? = null,
    override val result: OrderCaptureResponse.OrderCaptureData? = null
) : BaseCartResponse<OrderCaptureResponse.OrderCaptureData>()