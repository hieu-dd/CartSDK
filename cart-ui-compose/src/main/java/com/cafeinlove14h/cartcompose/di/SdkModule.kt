package com.cafeinlove14h.cartcompose.di

import com.cafeinlove14h.cartcompose.CartSdk
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import vn.teko.terra.core.android.terra.TerraApp

const val APP_NAME = "Test CartSDK"
val cartModule = DI.Module("cartModule") {
    bindSingleton {
        CartSdk.getInstance(
            instance(),
            TerraApp.getInstance(APP_NAME),
            "vnshop",
            "vnshop",
            "vnshop",
            6
        )
    }

    bindSingleton {
        CartSdk.getInstance(
            instance(),
            TerraApp.getInstance(APP_NAME),
            "vnshop",
            "vnshop",
            "vnshop",
            6
        ).getCartBus()
    }
}


