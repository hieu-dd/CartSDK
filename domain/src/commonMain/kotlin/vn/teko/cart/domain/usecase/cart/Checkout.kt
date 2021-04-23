package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Instant
import vn.teko.cart.domain.exception.CartError.CheckoutError.*
import vn.teko.cart.domain.exception.Error
import vn.teko.cart.domain.exception.ExternalServiceError
import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.model.OrderCaptureModel
import vn.teko.cart.domain.model.OrderCapturePayload.*
import vn.teko.cart.domain.model.OrderCaptureResponse
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.order.OrderDataSource
import vn.teko.cart.domain.usecase.service.ServiceDataSource.Companion.DELIVERY_SERVICE_GROUP

open class Checkout(
    private val cartDataSource: CartDataSource,
    private val orderDataSource: OrderDataSource
) : UseCase<Checkout.CheckoutParams, OrderCaptureResponse>() {

    override suspend fun run(params: CheckoutParams): OrderCaptureResponse = coroutineScope {
        val cartEntity = params.cartEntity
        val customerShippingInfo = cartDataSource.getShippingInfo(cartEntity.id) ?: throw ShippingInfoRequired
        val appliedServices = if (params.appliedServices.isNullOrEmpty()) {
            listOf(cartEntity.defaultDeliveryService!!)
        } else {
            params.appliedServices
        }

        val deliveryService =
            appliedServices.firstOrNull { it.groupId == DELIVERY_SERVICE_GROUP } ?: throw DeliveryServiceRequired
        /**
         * Update new shipping fee service from client
         */
        cartEntity.apply {
            val newShippingFee = deliveryService.fee
            grandTotal = grandTotal - shippingFee + newShippingFee
            shippingFee = newShippingFee
            serviceCode = deliveryService.code.orEmpty()
        }

        /**
         * Build a distinct Order Capture Model which contains terminal, shipping and customer info and merge it with current cart into the order payload
         */
        val payload = params.run {
            OrderCaptureModel(
                channelType = channelType,
                channelId = channelId,
                delivery = delivery,
                customer = customer,
                notes = notes,
                note = note,
                referralCode = referralCode,
                shippingInfo = customerShippingInfo,
                invoiceInfo = invoiceInfo,
                additionalShippingInfo = shippingInfo,
                terminalCode = terminalCode,
                channelCode = channelCode,
                expiryTime = expiryTime,
                appliedServices = appliedServices,
            ).from(cartEntity)
        }

        /**
         * Create order by delivery the above payload to the external service
         */
        val createdOrder = orderDataSource.createOrder(payload, params.userToken)

        if (createdOrder.error != null) {
            val error = createdOrder.error
            throw ExternalServiceError.OrderCaptureError.CreateOrderError(error.message)
        }

        /**
         * Delete all selected items and coupon after successfully checking out
         */
        cartDataSource.deleteSelectedItems(cartEntity.id)
        cartDataSource.deleteCouponPromotion(cartEntity.id)

        createdOrder
    }

    data class CheckoutParams(
        val cartEntity: CartEntity,
        val channelType: String,
        val channelId: Int,
        val delivery: Boolean,
        val customer: Customer,
        val userToken: String,
        val terminalCode: String,
        val channelCode: String,
        val notes: List<Note>? = listOf(),
        val note: String? = null,
        val referralCode: String? = null,
        val shippingInfo: AdditionalShippingInfo? = null,
        val invoiceInfo: InvoiceRequest? = null,
        val expiryTime: Instant? = null,
        val appliedServices: List<AppliedService>? = null,
    ) : UseCase.Params() {
        override fun selfValidate(): Error? {
            return when {
                cartEntity.items.none { it.isSelected } -> {
                    NoItemsToCheckout
                }
                else -> null
            }
        }
    }

}