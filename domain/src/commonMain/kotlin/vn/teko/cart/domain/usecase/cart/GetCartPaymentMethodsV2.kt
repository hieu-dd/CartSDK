package vn.teko.cart.domain.usecase.cart

import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.payment.PaymentDataSource
import vn.teko.cart.domain.usecase.payment.PaymentInformation
import vn.teko.cart.domain.util.getOrElse

/**
 * GetCartPaymentMethodsV2 is for getting the available payment methods for a target cart
 */
class GetCartPaymentMethodsV2(
    private val getCart: GetCart,
    private val paymentDataSource: PaymentDataSource,
) : UseCase<GetCartPaymentMethodsV2.GetCartPaymentMethodsV2Params, PaymentInformation>() {


    override suspend fun run(params: GetCartPaymentMethodsV2Params): PaymentInformation {
        val cart = getCart(
            GetCart.GetCartParam(
                cartId = params.cartId,
                userId = params.userId,
                userToken = params.userToken,
                terminalCode = params.terminal
            )
        ).getOrElse {
            if (it is CartError.CartNotFound) throw it else throw CartError.GetPaymentMethodsError.CannotGetCart
        }
        val total = cart.grandTotal

        if (total == 0L) {
            return PaymentInformation(
                merchantCode = "",
                paymentMethods = listOf(),
            )
        }

        return paymentDataSource.getPaymentMethodsV2(
            userId = params.userId,
            terminalCode = params.terminal,
            amount = total,
            items = cart.items.filter {
                it.isSelected
            }.map { item ->
                PaymentDataSource.Item(
                    sku = item.sku,
                    quantity = item.quantity,
                    price = item.unitPrice,
                )
            },
        )
    }

    data class GetCartPaymentMethodsV2Params(
        val cartId: String?,
        val terminal: String,
        val userId: String,
        val userToken: String,
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId),
            )
    }
}