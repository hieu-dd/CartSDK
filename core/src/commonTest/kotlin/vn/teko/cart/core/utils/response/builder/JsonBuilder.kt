package vn.teko.cart.core.utils.response.builder

import kotlinx.serialization.json.JsonElement

abstract class JsonBuilder {

    abstract fun build(): JsonElement

}