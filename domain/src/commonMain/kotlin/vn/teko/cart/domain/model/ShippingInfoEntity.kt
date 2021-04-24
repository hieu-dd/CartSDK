package vn.teko.cart.domain.model

import kotlinx.serialization.Serializable
import vn.teko.cart.docs.swagger.v3.oas.annotations.ExternalDocumentation
import vn.teko.cart.docs.swagger.v3.oas.annotations.Schema

@Serializable
data class ShippingInfoEntity(
    @Schema(
        description = "Ward code, it should synchronize with logistic data",
        minLength = 0,
        maxLength = 10,
        externalDocs = ExternalDocumentation()
    )
    val wardCode: String? = null,

    @Schema(
        description = "District code, it should synchronize with logistic data",
        minLength = 0,
        maxLength = 10,
        externalDocs = ExternalDocumentation()
    )
    val districtCode: String,

    @Schema(
        description = "Province code, it should synchronize with logistic data",
        minLength = 0,
        maxLength = 10,
        externalDocs = ExternalDocumentation()
    )
    val provinceCode: String,

    @Schema(
        description = "Ward name",
        minLength = 0,
        maxLength = 255,
        example = "Phường Thịnh Quang",
        externalDocs = ExternalDocumentation()
    )
    val wardName: String? = null,

    @Schema(
        description = "District name",
        minLength = 0,
        maxLength = 255,
        example = "Quận Đống Đa",
        externalDocs = ExternalDocumentation()
    )
    val districtName: String,

    @Schema(
        description = "District name",
        minLength = 0,
        maxLength = 255,
        example = "Hà Nội",
        externalDocs = ExternalDocumentation()
    )
    val provinceName: String,

    @Schema(
        description = "Specific address (alley, no of house..)",
        minLength = 0,
        maxLength = 255,
        example = "Số 5, ngõ 67, phố Thái Thịnh",
        externalDocs = ExternalDocumentation()
    )
    val address: String,

    @Schema(
        description = "Name of receiver",
        minLength = 0,
        maxLength = 255,
        example = "Anh Tiến",
        externalDocs = ExternalDocumentation()
    )
    val name: String,

    @Schema(
        description = "Email of receiver",
        minLength = 0,
        maxLength = 254,
        example = "tien.dv@teko.vn",
        pattern = ".+@.+\\..+",
        externalDocs = ExternalDocumentation()
    )
    val email: String? = null,

    @Schema(
        description = "Phone of receiver",
        minLength = 0,
        maxLength = 12,
        example = "0932372636",
        pattern = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$",
        externalDocs = ExternalDocumentation()
    )
    val telephone: String,

    @Schema(
        description = "Longitude of receiver",
        example = "123.437612",
        pattern = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)\$",
        externalDocs = ExternalDocumentation()
    )
    val longitude: Float? = null,

    @Schema(
        description = "Lattitude of receiver",
        example = "123.437612",
        pattern = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)\$",
        externalDocs = ExternalDocumentation()
    )
    val latitude: Float? = null,

    @Schema(
        description = "Additional note",
        minLength = 0,
        maxLength = 255,
        example = "Giao hàng sau 7h",
        externalDocs = ExternalDocumentation()
    )
    var addressNote: String? = null,

    @Schema(
        description = "Store code where the goods will be packed",
        minLength = 0,
        maxLength = 100,
        example = "CP07",
        externalDocs = ExternalDocumentation()
    )
    var storeCode: String? = null,

    @Schema(
        description = "Expected Time of Arrival",
        example = "17-01-2021T19:23:30",
        externalDocs = ExternalDocumentation()
    )
    var expectedDate: String? = null
)