package vn.teko.cart.domain.model

import vn.teko.cart.docs.swagger.v3.oas.annotations.ExternalDocumentation
import vn.teko.cart.docs.swagger.v3.oas.annotations.Schema

data class CartConfig(
    @Schema(
        description = "terminal",
        externalDocs = ExternalDocumentation()
    )
    val terminal: String,

    @Schema(
        description = "channel",
        externalDocs = ExternalDocumentation()
    )
    val channel: String,
)
