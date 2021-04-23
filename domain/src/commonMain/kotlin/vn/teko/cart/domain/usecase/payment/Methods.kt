package vn.teko.cart.domain.usecase.payment

import kotlinx.serialization.Serializable

@Serializable
data class Deposit(
    val allowDeposit: Boolean,
    val payNowAmount: String?,
    val applyDepositIds: List<String> = listOf()
)

@Serializable
data class Method(
    val partnerCode: String,
    val methodCode: String,
    val displayName: String,
    val displayText: String,
    val displayIcon: String,
    val methodGroupCode: String,
    val merchantMethodCode: String,
    val token: String? = null,
    val discountCodes: List<String> = listOf()
)

@Serializable
data class PaymentInformation(
    val merchantCode: String,
    val paymentMethods: List<Method>,
    val deposit: Deposit? = null
)