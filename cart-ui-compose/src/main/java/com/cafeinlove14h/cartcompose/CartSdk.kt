package com.cafeinlove14h.cartcompose

import android.content.Context
import vn.teko.cart.android.bus.CartBus
import vn.teko.terra.core.android.terra.TerraApp

class CartSdk private constructor(
    context: Context,
    terraApp: TerraApp,
    terminal: String,
    channel: String,
    channelType: String,
    channelId: Int,
) {
    private val cartBus = CartBus.getInstance(context, terraApp, terminal, channel, channelType, channelId)
    fun getCartBus() = cartBus
    private var navigationDelegate: CartNavigationDelegate? = null

    fun setNavigationDelegate(navigationDelegate: CartNavigationDelegate?) = apply {
        this.navigationDelegate = navigationDelegate
    }

    internal fun getNavigationDelegate() = navigationDelegate

    companion object {
        private val cartSdkMap: MutableMap<String, CartSdk> = mutableMapOf()

        @JvmStatic
        fun getInstance(
            context: Context,
            terraApp: TerraApp,
            terminal: String,
            channel: String,
            channelType: String,
            channelId: Int,
        ): CartSdk {
            return cartSdkMap.getOrPut(terraApp.appName) {
                CartSdk(context, terraApp, terminal, channel, channelType, channelId)
            }
        }

        @JvmStatic
        internal fun getInstance(terraApp: TerraApp): CartSdk =
            cartSdkMap[terraApp.appName] ?: throw Throwable("CartSdk has not been initialized")
    }
}