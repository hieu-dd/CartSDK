package vn.teko.cart.domain.model

import kotlinx.datetime.Instant
import vn.teko.cart.docs.swagger.v3.oas.annotations.ExternalDocumentation
import vn.teko.cart.docs.swagger.v3.oas.annotations.Schema

data class CartData(
    @Schema(description = "", externalDocs = ExternalDocumentation())
    val product: ProductEntity,

    @Schema(description = "", externalDocs = ExternalDocumentation(), nullable = true)
    val selectablePromotionId: Int?,

    @Schema(description = "", externalDocs = ExternalDocumentation(), nullable = true)
    val couponPromotionId: Int?,

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val quantity: Int,

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val selected: Boolean,

    @Schema(description = "", externalDocs = ExternalDocumentation(), hidden = true)
    val addedAt: Instant,

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val id: String,

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val consultationServiceFee: Double?
)