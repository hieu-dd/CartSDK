package vn.teko.cart.docs.swagger.v3.oas.annotations

import vn.teko.cart.docs.swagger.v3.oas.annotations.extensions.Extension

actual annotation class ExternalDocumentation actual constructor(
    actual val description: String,
    actual val url: String,
    actual val extensions: Array<Extension>
)
