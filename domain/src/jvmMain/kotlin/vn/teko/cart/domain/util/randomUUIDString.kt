package vn.teko.cart.domain.util

import java.util.*


internal actual fun randomUUIDString(): String {
    return UUID.randomUUID()
        .toString()
}