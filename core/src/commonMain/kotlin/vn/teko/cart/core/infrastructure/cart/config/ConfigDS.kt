package vn.teko.cart.core.infrastructure.cart.config

import vn.teko.cart.core.CartConfig

internal class ConfigDS(private val configRepo: ConfigRepo) : ConfigDataSource {
    override suspend fun getConfig(): CartConfig = CartConfig.fromConfig(configRepo.getConfig()!!)

    override suspend fun saveConfig(cartConfig: CartConfig) =
        when (val existConfig = configRepo.getConfig()) {
            null -> configRepo.createOrUpdate(CartConfig.createDBConfig(cartConfig = cartConfig))
            else -> configRepo.createOrUpdate(
                CartConfig.createDBConfig(
                    id = existConfig.id,
                    cartConfig = cartConfig
                )
            )
        }
}