package vn.teko.cart.core.infrastructure.cart.config

import vn.teko.cart.core.CartConfig

internal interface ConfigDataSource {
    suspend fun getConfig(): CartConfig

    suspend fun saveConfig(cartConfig: CartConfig)
}