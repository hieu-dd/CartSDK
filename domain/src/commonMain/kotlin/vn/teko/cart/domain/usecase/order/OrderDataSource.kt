package vn.teko.cart.domain.usecase.order

import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.OrderCapturePayload
import vn.teko.cart.domain.model.OrderCaptureResponse

/**
 * OrderDataSource contains order-related information
 */
interface OrderDataSource {

    /**
     *  Similar but is from order capture
     */
    @Serializable
    data class OrderCaptureDetail(
        val id: String,
        val code: String,
        val grandTotal: String,
        val createdAt: String,
        val items: List<OrderCaptureItem> = listOf(),
        val payments: List<OrderCapturePayment>? = listOf(),
        val shippingInfo : OrderShippingInfo? = null
    ) {
        @Serializable
        data class OrderCaptureItem(
            val lineItemId: String,
            val sellerId: Int,
            val quantity: Int,
            val sku: String,
            val unitPrice: String,
            val name: String? = null,
            val displayName: String? = null,
            val taxCode: String? = null,
            val unit: String? = null,
            val bundleCode: String? = null,
            val siteId: String? = null,
            val serial: String? = null,
            val vat: String? = null,
            val unitAdd: String? = null,
            val warranty: String? = null,
            val rowTotal: String? = null,
        )

        @Serializable
        data class OrderCapturePayment(
            val transactionCode: String,
            val method: String,
            val createdAt: String,
            val paidAt: String,
            val totalPaid: String,
            val amount: String,
            val merchantCode: String,
            val partnerId: String? = "",
            val paymentDuration: String? = "0",
            val partnerTransactionCode: String? = ""
        )

        @Serializable
        data class OrderShippingInfo(
            val shipDate: String? = null,
            val shipStreet: String? = null,
            val shipPhone: String? = null,
            val shipEmail: String? = null,
            val shipCountry: String? = null,
            val shipNote: String? = null,
            val shipStoreCode: String? = null,
            val shipAddressCode: String? = null,
            val shipName: String? = null,
            val fullAddress: String? = null,
            val districtId: String? = null,
            val provinceId: String? = null,
            val wardId: String? = null,
        )
    }

    suspend fun createOrder(payload: OrderCapturePayload, token: String): OrderCaptureResponse

    suspend fun getOrderCaptureDetail(code: String, userToken: String): OrderCaptureDetail
}
