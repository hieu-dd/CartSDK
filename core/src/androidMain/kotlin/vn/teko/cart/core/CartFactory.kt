package vn.teko.cart.core

import android.content.Context
import org.kodein.di.bind
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.singleton
import vn.teko.cart.core.di.DataModule
import vn.teko.cart.core.di.PlatformModule
import vn.teko.cart.core.infrastructure.cart.user.User

actual object CartFactory {
    fun create(context: Context, cartConfig: CartConfig): CartManager {
        val di = ConfigurableDI(mutable = true)
        di.addConfig {
            bind<User>() with singleton { User() }
            bind<Context>() with singleton { context.applicationContext }
            import(DataModule, true)
            import(PlatformModule, true)
        }
        return CartManager(di).apply { updateConfig(cartConfig)}
    }
}