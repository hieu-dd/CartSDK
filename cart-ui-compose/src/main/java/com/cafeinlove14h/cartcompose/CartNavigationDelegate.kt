package com.cafeinlove14h.cartcompose

import vn.teko.cart.android.bus.model.request.PaymentRequest

interface CartNavigationDelegate {
    fun backFromCart()
    fun goToLogin()
    fun goToAddress()
    fun goToBuy()
    fun goToCustomer(customerId: String?)
    fun goToShippingAddress(customerId: String)
    fun goToPayment(paymentRequest: PaymentRequest)
}