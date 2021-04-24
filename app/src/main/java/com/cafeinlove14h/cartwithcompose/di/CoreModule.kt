package com.cafeinlove14h.cartwithcompose.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.teko.cart.core.CartFactory
import vn.teko.cart.core.CartManager
import vn.teko.cart.core.cartConfig

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    fun provideCartManager(@ApplicationContext context: Context): CartManager {
        return CartFactory.create(context, cartConfig {
            baseUrl = "https://carts-beta.stag.tekoapis.net"
            tenant = "vnshop"
            terminal = "vnshop"
            channel = "vnshop"
        })
    }
}