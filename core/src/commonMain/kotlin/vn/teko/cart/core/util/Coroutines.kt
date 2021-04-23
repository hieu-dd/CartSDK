package vn.teko.cart.core.util

import kotlinx.coroutines.CoroutineScope

expect fun <T> runBlockingPlatform(block: suspend CoroutineScope.() -> T)
