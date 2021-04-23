package vn.teko.cart.core

import org.kodein.di.bind
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.singleton
import vn.teko.cart.core.di.DataModule
import vn.teko.cart.core.di.PlatformModule
import vn.teko.cart.core.infrastructure.cart.user.User
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
actual object CartFactory {
    fun create(cartConfig: CartConfig): CartManager {
        val kodein = ConfigurableDI(mutable = true)
        kodein.addConfig {
            bind<User>() with singleton { User() }
            import(DataModule, true)
            import(PlatformModule, true)
        }
        return CartManager(kodein).apply { updateConfig(cartConfig) }
    }
}