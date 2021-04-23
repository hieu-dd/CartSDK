package vn.teko.cart.core

import kotlinx.coroutines.CoroutineDispatcher

internal expect fun backgroundDispatcher(): CoroutineDispatcher

internal expect fun dispatcher(): CoroutineDispatcher