package vn.teko.cart.domain.model

import kotlinx.serialization.Serializable
import vn.teko.cart.docs.swagger.v3.oas.annotations.ExternalDocumentation
import vn.teko.cart.docs.swagger.v3.oas.annotations.Schema


@Serializable
data class CartTokenEntity(
    @Schema(description = "Cart Token", externalDocs = ExternalDocumentation())
    val token: String,
)
