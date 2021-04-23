package vn.teko.cart.domain.model

import kotlinx.datetime.Instant
import vn.teko.cart.domain.model.OrderCapturePayload.*

data class OrderCaptureModel(
    val channelType: String,
    val channelId: Int,
    val delivery: Boolean,
    val customer: Customer,
    val terminalCode: String,
    val channelCode: String,
    val shippingInfo: ShippingInfoEntity,
    val notes: List<Note>?,
    val note: String? = null,
    val referralCode: String?,
    val additionalShippingInfo: AdditionalShippingInfo?,
    val invoiceInfo: InvoiceRequest?,
    val expiryTime: Instant?,
    val appliedServices: List<AppliedService>? = null,
) {
    fun from(cart: CartEntity): OrderCapturePayload {
        val selectedProducts = cart.items.filter { it.isSelected }
        val products = selectedProducts.map { Item.from(it) }
        val promotions = cart.promotions.filter { it.isSelected }.map { Promotion.from(it) }
        /**
         * Fall back for Empty Customer Name
        */
        customer.name = customer.name.ifBlank { shippingInfo.name }

        return OrderCapturePayload(
            promotions = promotions,
            items = products,
            channelType = channelType,
            channelId = channelId,
            customer = customer,
            referralCode = referralCode,
            delivery = delivery,
            grandTotal = cart.grandTotal.toDouble(),
            terminalCode = terminalCode,
            channelCode = channelCode,
            shippingInfo = ShippingInfo.from(shippingInfo, additionalShippingInfo),
            notes = notes?.associate { it.sellerId to it.value },
            note = note,
            billingInfo = BillingInfo.from(invoiceInfo),
            expiryTime = expiryTime,
            services = appliedServices,
        )
    }
}