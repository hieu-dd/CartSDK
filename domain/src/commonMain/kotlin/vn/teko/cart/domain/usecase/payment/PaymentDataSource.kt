package vn.teko.cart.domain.usecase.payment

/**
 * PaymentDataSource contains payment-related information
 */
interface PaymentDataSource {
    /**
     * Item stand for a line of item
     * @param[sku] stock keeping unit
     * @param[quantity] purchased quantity
     * @param[price] sell price of product (before applying any promotions)
     */
    data class Item(
        val sku: String,
        val quantity: Int,
        val price: Long,
    )

    /**
     * return payment methods available from provided cart or order information
     * @param[userId] identifier of the cart buyer
     * @param[terminalCode] code of processing terminal
     * @param[amount] total need to paid amount
     * @param[items] processing item information
     * @return a JsonObject (forwarding raw data from PS, need not understand)
     */
    suspend fun getPaymentMethodsV2(
        userId: String?,
        terminalCode: String,
        amount: Long,
        items: List<Item>,
    ): PaymentInformation
}