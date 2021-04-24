package vn.teko.cart.core.extension

import org.kodein.di.bind
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.singleton
import vn.teko.cart.core.CartConfig
import vn.teko.cart.core.CartFactory
import vn.teko.cart.core.CartManager
import vn.teko.cart.core.di.DataModule
import vn.teko.cart.core.di.PlatformModuleTest
import vn.teko.cart.core.infrastructure.cart.user.User

internal fun CartFactory.createTestCart(cartConfig: CartConfig): CartManager {
    val kodein = ConfigurableDI(mutable = true)
    kodein.addConfig {
        bind<User>() with singleton { User() }
        import(DataModule, true)
        import(PlatformModuleTest, true)
    }
    return CartManager(kodein).also { it.updateConfig(cartConfig) }
}