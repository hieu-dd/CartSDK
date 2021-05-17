package vn.teko.cart.android.busmodels

data class AddProductToCartRequest(
    val sku: String,
    val selectPromotionId: Int? = null
)