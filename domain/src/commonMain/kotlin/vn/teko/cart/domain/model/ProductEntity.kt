package vn.teko.cart.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import vn.teko.cart.docs.swagger.v3.oas.annotations.ExternalDocumentation

import vn.teko.cart.docs.swagger.v3.oas.annotations.Schema
import vn.teko.cart.domain.util.PlatformDateFromTimeStampSerializer

@Serializable
data class ProductEntity(
    @Schema(description = "", externalDocs = ExternalDocumentation())
    val prices: List<ProductPriceEntity>,

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val productInfo: ProductInfoEntity,

    @Schema(description = "", externalDocs = ExternalDocumentation(), minimum = "0")
    val totalAvailable: Int?,

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val provider: ProviderEntity,

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val promotions: List<PromotionEntity> = listOf(),

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val flashSales: List<FlashSaleEntity> = listOf(),

    @Schema(description = "", externalDocs = ExternalDocumentation())
    val status: ProductStatusEntity
)

@Serializable
data class ProviderEntity(
    val id: Int,

    val name: String? = null,

    val logo: String? = null,

    val slogan: String? = null,
)

@Serializable
data class ProductPriceEntity(
    val sellPrice: Long,

    val supplierRetailPrice: Long,
)

@Serializable
data class ProductInfoEntity(
    val sku: String,

    val name: String,

    val seller: SellerEntity,

    val imageUrl: String?,

    val tax: TaxEntity?,

    val warranty: WarrantyEntity?,

    val masterCategories: List<Category> = listOf(),

    val categories: List<Category> = listOf(),
) {

    @Serializable
    data class Category(
        val id: Int,
        val code: String,
        val name: String,
    )

    @Serializable
    data class SellerEntity(
        val id: Int,

        val name: String?,

        val displayName: String?,

        val logo: String?
    )

    @Serializable
    data class TaxEntity(
        val taxOut: Long?,
        val taxOutCode: String?
    )

    @Serializable
    data class WarrantyEntity(
        val months: Long?,

        val description: String?
    )
}

@Serializable
data class FlashSaleEntity(
    var id: Int = 0,

    var type: String = "",

    var discountPercent: Double = 0.0,

    var usedCount: Int = 0,

    var totalCount: Int = 0,

    @Serializable(with = PlatformDateFromTimeStampSerializer::class)
    var startTimestampSec: Instant? = null,

    @Serializable(with = PlatformDateFromTimeStampSerializer::class)
    var endTimestampSec: Instant? = null
)

@Serializable
data class ProductStatusEntity(
    var sellingCode: String,

    var sellable: Boolean
)