package vn.teko.cart.domain.usecase.order

import vn.teko.cart.domain.usecase.StringValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.payment.PaymentDataSource
import vn.teko.cart.domain.usecase.payment.PaymentInformation

/**
 * GetPaymentMethodsV2 is for getting the available payment methods for a a cart or
 */
class GetOrderPaymentMethodsV2(
    private val orderDataSource: OrderDataSource,
    private val paymentDataSource: PaymentDataSource,
) : UseCase<GetOrderPaymentMethodsV2.GetOrderPaymentMethodsV2Params, PaymentInformation>() {
    override suspend fun run(params: GetOrderPaymentMethodsV2Params): PaymentInformation {
        val order = orderDataSource.getOrderCaptureDetail(
            code = params.orderCode,
            userToken = params.userToken,
        )

        val grandTotal = order.grandTotal.toLong()
        if (grandTotal == 0L) {
            return PaymentInformation(
                merchantCode = "",
                paymentMethods = listOf(),
            )
        }
        return paymentDataSource.getPaymentMethodsV2(
            userId = params.userId,
            terminalCode = params.terminal,
            amount = grandTotal,
            items = order.items.map { item ->
                PaymentDataSource.Item(
                    sku = item.sku,
                    quantity = item.quantity.toInt(),
                    price = item.unitPrice.toLong(),
                )
            },
        )

    }

    data class GetOrderPaymentMethodsV2Params(
        val orderCode: String,
        val terminal: String,
        val userId: String,
        val userToken: String,
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                StringValidator(orderCode, "orderCode"),
                StringValidator(terminal, "terminal"),
            )
    }
}