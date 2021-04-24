package vn.teko.cart.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual fun backgroundDispatcher(): CoroutineDispatcher = Dispatchers.Default

internal actual fun dispatcher(): CoroutineDispatcher = Dispatchers.Main