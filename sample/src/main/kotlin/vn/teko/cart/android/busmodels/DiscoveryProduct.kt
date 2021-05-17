package vn.teko.cart.android.busmodels

import kotlinx.serialization.Serializable

@Serializable
data class DiscoveryProduct(
    val prices: List<ProductPrice>,
    val productInfo: ProductInfo,
    val promotions: List<Promotion>,
    val totalAvailable: Int?,
    val productDetail: ProductDetail? = null
)

@Serializable
data class ProductPrice(
    val sellPrice: Long,
    val supplierRetailPrice: Long,
)

@Serializable
data class ProductInfo(
    val sku: String,
    val name: String,
    val seller: Seller,
    val imageUrl: String?,
) {
    @Serializable
    data class Seller(
        val id: Int,
        val name: String?,
        val displayName: String?,
        val logo: String?
    )
}

@Serializable
data class Promotion(
    val id: Int,
    val benefit: Benefit = Benefit(),
    var applyOn: String,
) {
    @Serializable
    data class Benefit(
        var discount: Discount? = null,
        var voucher: Voucher? = null,
        var gifts: List<Gift>? = null
    ) {
        @Serializable
        data class Voucher(
            var name: String = "",
            var quantity: Int = 0,
            var maxQuantity: Int,
        )

        @Serializable
        data class Discount(
            var percent: Double? = null,
            var maxAmount: Double? = null,
            var flat: Double? = null,
            var maxAmountPerOrder: Long? = null,
        )

        @Serializable
        data class Gift(
            var sku: String = "",
            var name: String = "",
            var imageUrl: String = "",
            var quantity: Int = 0,
            var maxQuantityPerOrder: Int? = null,
        )
    }
}

@Serializable
data class ProductDetail(
    val productGroup: ProductGroup?
)

@Serializable
data class ProductGroup(
    val variants: List<Variant> = listOf()
)

@Serializable
data class Variant(
    val attributeValues: List<AttributeValue> = listOf()
)

@Serializable
data class AttributeValue(
    val id: Int,
    val code: String,
    val value: String,
    val optionId: Int
)
