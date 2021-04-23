package vn.teko.cart.core.infrastructure.cart.error

import vn.teko.cart.domain.exception.Error

open class CartError(code: Int, message: String) : Error.FeatureError(code, message, null)

object CartUnspecifiedError : CartError(0, "An unspecified error occurred")