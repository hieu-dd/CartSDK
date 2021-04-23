package vn.teko.cart.docs.swagger.v3.oas.annotations

import vn.teko.cart.docs.swagger.v3.oas.annotations.extensions.Extension

expect annotation class ExternalDocumentation(
    val description: String = "",
    val url: String = "",
    val extensions: Array<Extension> = []
)
