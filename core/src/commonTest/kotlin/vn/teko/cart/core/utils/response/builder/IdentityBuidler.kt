package vn.teko.cart.core.utils.response.builder

import kotlinx.serialization.json.*

@DslMarker
annotation class CartDsl

@CartDsl
class IdentityResponseBuilder internal constructor() : JsonBuilder() {
    var statusCode: Int? = null
    var error: String? = null

    private var result: JsonElement? = null

    fun result(block: IdentityResultBuilder.() -> Unit) {
        result = IdentityResultBuilder().apply(block).build()
    }

    override fun build(): JsonElement = buildJsonObject {
        put("statusCode", statusCode)
        put("error", error)
        put("result", result ?: JsonNull)
    }
}

@CartDsl
class IdentityResultBuilder internal constructor() : JsonBuilder() {
    var token: String? = null

    override fun build(): JsonElement = buildJsonObject {
        put("token", token)
    }
}

@CartDsl
fun identityResponse(block: IdentityResponseBuilder.() -> Unit): String {
    return IdentityResponseBuilder().apply(block).build().toString()
}
