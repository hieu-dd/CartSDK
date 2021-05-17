package vn.teko.cart.android.busmodels

data class CustomerProfile(
    val id: Int,
    val customerUserId: Int,
    val name: String,
    val phone: String,
    val email: String?,
    val address: String?,
    val wardId: String?,
    val districtId: String?,
    val provinceId: String?,
    val asiaCrmId: String?,
    val fullAddress: String?,
    val shippingInfo: ShippingInfo?,
    val isRetail: Boolean,
) {
    data class ShippingInfo(
        val id: Int,
        val name: String,
        val phone: String,
        val address: String?,
        val fullAddress: String,
        val wardId: String?,
        val wardName: String?,
        val districtId: String,
        val districtName: String,
        val provinceId: String,
        val provinceName: String,
        val email: String?,
        val note: String?,
        val country: String?,
        val type: Int?,
        val latitude: Float?,
        val longitude: Float?,
        val storeCode: String?,
    )
}

fun customer(name: String, isCompany: Boolean, isRetail: Boolean, hasShipping: Boolean) =
    CustomerProfile(
        id = 24196,
        customerUserId = 24196,
        name = if (isRetail) "Khách lẻ" else name,
        "0943310394",
        "hieu.td@teko.vn",
        "36 Hoang Cau",
        "010104",
        "0101",
        "01",
        null,
        "Full address",
        if (hasShipping && !isRetail) shippingInfo() else null,
        isRetail,
    )

fun shippingInfo() = CustomerProfile.ShippingInfo(
    1,
    "Đỗ Đức Hiếu",
    "0943310394",
    "36 Hoàng Cầu",
    "Full address",
    "010104",
    "Phường Cống Vị",
    "0101",
    "Quận Ba Đình",
    "01",
    "Thành phố Hà Nội",
    "hieu.td@teko.vn",
    "",
    "Việt Nam",
    null,
    null,
    null,
    null
)
