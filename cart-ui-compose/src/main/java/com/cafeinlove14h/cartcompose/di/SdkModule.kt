package com.cafeinlove14h.cartcompose.di

import android.app.Activity
import android.content.Context
import com.cafeinlove14h.cartcompose.CartSdk
import com.cafeinlove14h.cartcompose.extensions.getAppName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import vn.teko.cart.android.bus.CartBus
import vn.teko.terra.core.android.terra.TerraApp

@Module
@InstallIn(SingletonComponent::class)
object SdkModule {
    @Provides
    fun provideCartSdk(): CartSdk {
        return CartSdk.getInstance(TerraApp.getInstance(APP_NAME))
    }

    @Provides
    fun provideCartBus(): CartBus {
        return CartSdk.getInstance(TerraApp.getInstance(APP_NAME)).getCartBus()
    }

    const val APP_NAME = "Test CartSDK"
}

