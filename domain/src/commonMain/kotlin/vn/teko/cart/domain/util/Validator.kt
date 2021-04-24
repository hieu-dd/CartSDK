package vn.teko.cart.domain.util

const val UUID_REGEXP = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"

internal fun String?.isUUID(): Boolean {
    return this?.matches(UUID_REGEXP.toRegex()) ?: false
}
