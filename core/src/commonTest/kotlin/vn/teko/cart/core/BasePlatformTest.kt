package vn.teko.cart.core

import kotlinx.coroutines.CoroutineScope

expect abstract class BasePlatformTest(){
    fun <T> runTest(block: suspend CoroutineScope.() -> T)
    fun initCartManager(cartConfig: CartConfig)
    fun getCartManager(): CartManager
}