package vn.teko.cart.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.CoreFoundation.CFRunLoopGetCurrent
import platform.CoreFoundation.CFRunLoopRun
import platform.CoreFoundation.CFRunLoopStop
import vn.teko.cart.core.extension.createTestCart

actual abstract class BasePlatformTest {
    lateinit var cartManager: CartManager

    actual fun <T> runTest(block: suspend CoroutineScope.() -> T) {
        var error: Throwable? = null
        GlobalScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (t: Throwable) {
                error = t
            } finally {
                CFRunLoopStop(CFRunLoopGetCurrent())
            }
        }
        CFRunLoopRun()
        error?.also { throw it }
    }

    actual fun initCartManager(cartConfig: CartConfig) {
        cartManager = CartFactory.createTestCart(cartConfig)
    }

    actual fun getCartManager() = cartManager
}