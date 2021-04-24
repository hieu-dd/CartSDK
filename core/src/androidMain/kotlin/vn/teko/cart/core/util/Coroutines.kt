package vn.teko.cart.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun <T> runBlockingPlatform(block: suspend CoroutineScope.() -> T){
    return runBlocking {
        block()
    }
}