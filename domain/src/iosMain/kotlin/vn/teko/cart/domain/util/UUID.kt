package vn.teko.cart.domain.util

import platform.Foundation.NSUUID

actual fun randomUUIDString(): String {
    return NSUUID().UUIDString.toLowerCase()
}

internal fun Long.toSeconds(): Long {
    return this / 1_000
}

internal fun Long.toMilliseconds(): Long {
    return this * 1_000
}