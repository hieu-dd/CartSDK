package vn.teko.cart.core.infrastructure.cart.response


abstract class BaseCartResponse<DATA> {
    abstract val statusCode: Int

    abstract val error: String?

    open val result: DATA? = null

    fun isSuccess() = statusCode == 0
}