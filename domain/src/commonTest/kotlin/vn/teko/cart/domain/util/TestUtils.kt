package vn.teko.cart.domain.util

import kotlinx.coroutines.CoroutineScope


expect fun runTest(block: suspend (scope: CoroutineScope) -> Unit)
