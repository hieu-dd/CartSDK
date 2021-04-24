package vn.teko.cart.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderCaptureResponse(
    val code: Int,
    val data: OrderCaptureData? = null,
    val error: OrderCaptureError? = null
) {
    @Serializable
    data class OrderCaptureError(
        val traceCode: String,
        val message: String,
        val orderCode: String,
        val orderId: String
    )

    @Serializable
    data class OrderCaptureData(
        val code: String,
        val grandTotal: String,
        val createdAt: String,
        val predictCancelAt: String,
        val orderId: String
    )
}


@Serializable
data class OrderCaptureError(
    val error: String,
    val message: String
)
