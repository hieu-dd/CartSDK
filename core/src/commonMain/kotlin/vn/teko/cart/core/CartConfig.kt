package vn.teko.cart.core

import vn.teko.cart.core.db.Config

class CartConfig internal constructor(
    val baseUrl: String,
    val terminal: String,
    val channel: String,
    val tenant: String
) {
    companion object {
        fun fromConfig(config: Config) = with(config) {
            CartConfig(baseUrl, terminal, channel, tenant)
        }

        fun createDBConfig(id: Long? = null, cartConfig: CartConfig): Config = with(cartConfig) {
            Config(id ?: 0, baseUrl, terminal, channel, tenant, null, null)
        }
    }
}

class CartConfigBuilder internal constructor() {

    var baseUrl: String? = null

    var terminal: String? = null

    var channel: String? = null

    var tenant: String? = null


    fun build(): CartConfig {
        return when {
            baseUrl == null -> throw CartConfigError("baseUrl must be set")
            terminal == null -> throw CartConfigError("terminal must be set")
            channel == null -> throw CartConfigError("channel must be set")
            tenant == null -> throw CartConfigError("tenant must be set")
            else -> CartConfig(baseUrl!!, terminal!!, channel!!, tenant!!)
        }
    }
}

class CartConfigError(override val message: String?) : Throwable(message)

fun cartConfig(
    block: CartConfigBuilder.() -> Unit
): CartConfig = CartConfigBuilder().apply(block).build()