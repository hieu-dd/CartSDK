package vn.teko.cart.core.infrastructure.cart.request

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.OrderCapturePayload
import vn.teko.cart.domain.util.RFC3339DateTimeSerializer

@Serializable
data class CheckoutRequest(
    val isDelivery: Boolean? = true,

    val invoiceInfo: OrderCapturePayload.InvoiceRequest? = null,

    val referralCode: String? = null,

    val notes: List<OrderCapturePayload.Note>? = listOf(),

    val customer: OrderCapturePayload.Customer,

    val shippingInfo: OrderCapturePayload.AdditionalShippingInfo? = null,

    val channelType: String,

    val channelId: Int,

    @Serializable(with = RFC3339DateTimeSerializer::class)
    val expiryTime: Instant? = null,
)