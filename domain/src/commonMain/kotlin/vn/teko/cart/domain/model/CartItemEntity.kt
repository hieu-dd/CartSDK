package vn.teko.cart.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import vn.teko.cart.docs.swagger.v3.oas.annotations.ExternalDocumentation
import vn.teko.cart.docs.swagger.v3.oas.annotations.Schema
import vn.teko.cart.domain.util.RFC3339DateTimeSerializer
import vn.teko.cart.domain.util.UUID_REGEXP
import vn.teko.cart.domain.constants.SHIPPING_AND_TECHNICAL_SUPPORT_SKU_PHONGVU
import vn.teko.cart.domain.constants.SHIPPING_SKU_PHONGVU
import vn.teko.cart.domain.constants.SHIPPING_SKU_VINMART
import vn.teko.cart.domain.util.randomUUIDString
import kotlin.math.floor

@Serializable
data class CartItemEntity constructor(
    @Schema(minLength = 1, maxLength = 32, externalDocs = ExternalDocumentation(), pattern = UUID_REGEXP)
    var id: String = "",

    @Schema(
        description = "Unique ID generated using UUID",
        minLength = 1,
        maxLength = 32,
        externalDocs = ExternalDocumentation()
    )
    var lineItemId: String = "",

    @Schema(
        description = "Product's sku, obtained from Seller Center",
        minimum = "0",
        maximum = "32",
        externalDocs = ExternalDocumentation()
    )
    var sku: String,

    @Schema(description = "Product's name", minLength = 0, maxLength = 256, externalDocs = ExternalDocumentation())
    val name: String = "",

    @Schema(
        description = "Product's display name, used for SEO",
        minLength = 0,
        maxLength = 256,
        externalDocs = ExternalDocumentation()
    )
    val displayName: String = "",

    @Schema(description = "Quantity of this line item", externalDocs = ExternalDocumentation())
    var quantity: Int = 0,

    @Schema(description = "Original Price of the product", externalDocs = ExternalDocumentation())
    val unitPrice: Long = 0L,

    @Schema(description = "Discount amount of this line item", externalDocs = ExternalDocumentation())
    var unitDiscount: Long = 0L,

    @Schema(
        description = "Reason for the discount on unitDiscount",
        minLength = 0,
        maxLength = 256,
        externalDocs = ExternalDocumentation()
    )
    val reasonDiscount: String = "",

    @Schema(
        description = "Amount added to original price, a representation for commission",
        externalDocs = ExternalDocumentation()
    )
    val unitAdd: Long = 0L,

    @Schema(
        description = "Explanation for the unitAdd amount",
        minimum = "0",
        maximum = "256",
        externalDocs = ExternalDocumentation()
    )
    val reasonAdd: String = "",

    @Schema(description = "Final price of the line item", externalDocs = ExternalDocumentation())
    var price: Long = 0L,

    @Schema(description = "Value-added tax value for this product", externalDocs = ExternalDocumentation())
    var vatRate: Long = 0L,

    @Schema(description = "Total value of this line item ~ price * quantity", externalDocs = ExternalDocumentation())
    var rowTotal: Long = 0L,

    @Schema(description = "Id of the seller which this product belongs to", externalDocs = ExternalDocumentation())
    val sellerId: Int,

    @Schema(description = "Selection state of this line item in cart", externalDocs = ExternalDocumentation())
    var isSelected: Boolean = true,

    @Schema(
        description = "The original product object",
        externalDocs = ExternalDocumentation(),
        hidden = true,
        nullable = true
    )
    val product: ProductEntity?,

    @Serializable(with = RFC3339DateTimeSerializer::class)
    @Schema(description = "The time when this line item was added to cart", externalDocs = ExternalDocumentation())
    var addedAt: Instant,

    @Schema(description = "This line item is a gift or not", externalDocs = ExternalDocumentation())
    val isGift: Boolean = false,

    @Schema(
        description = "Expected promotion which uses for block-size type promotion",
        externalDocs = ExternalDocumentation(),
        nullable = true
    )
    var expectedPromotion: PromotionEntity? = null,

    @Schema(
        description = "Expected promotion (coupon) which uses for block-size type promotion",
        externalDocs = ExternalDocumentation(),
        nullable = true
    )
    @Transient
    var expectedCouponPromotion: PromotionEntity? = null,

    @Schema(
        description = "This line item is a service sku or not. In case of Phong Vu, the shipping fee also appears in invoice as a line",
        externalDocs = ExternalDocumentation()
    )
    val isShippingFeeItem: Boolean = false,

    @Schema(
        description = "The consultation service fee",
        externalDocs = ExternalDocumentation()
    )
    var consultationServiceFee: Double = 0.0,

    @Transient
    @Schema(
        description = "Used only when saving this line item to database, as the lineItemId generated from client is also accepted",
        externalDocs = ExternalDocumentation(),
        hidden = true
    )
    var isNew: Boolean = true
) {

    companion object {
        internal val SERVICE_SKUS = listOf(
            SHIPPING_SKU_PHONGVU,
            SHIPPING_SKU_VINMART,
            SHIPPING_AND_TECHNICAL_SUPPORT_SKU_PHONGVU
        )
    }

    init {
        if (id.isEmpty()) {
            id = randomUUIDString()
        }
        lineItemId = id
        calculatePrices()
    }

    fun belongTo(sellerId: Int): Boolean {
        return this.sellerId == sellerId
    }

    internal fun calculatePrices(appliedPromotions: List<CartPromotionEntity> = listOf()) {
        unitDiscount = floor(appliedPromotions.sumByDouble { it.discount } / quantity).toLong()

        price = unitPrice + unitAdd + consultationServiceFee.toLong() - unitDiscount
        rowTotal = price * quantity
    }

    fun isServiceFee() = SERVICE_SKUS.contains(sku) || isShippingFeeItem
}