package vn.teko.cart.domain.usecase.shippinginfo

import vn.teko.cart.domain.model.ShippingInfoEntity
import vn.teko.cart.domain.usecase.*
import vn.teko.cart.domain.usecase.Validator.Companion.CODE_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.CODE_MIN_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.NAME_MIN_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.STRING_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.STRING_MIN_LENGTH
import vn.teko.cart.domain.usecase.cart.CartDataSource

class AddShippingInfo(
    private val cartDataSource: CartDataSource
) : UseCase<AddShippingInfo.AddShippingInfoParams, ShippingInfoEntity>() {

    override suspend fun run(params: AddShippingInfoParams): ShippingInfoEntity = params.run {
        val cart = cartDataSource.getCart(cartId, userId)
        cartDataSource.addShippingInfo(cart.id, shippingInfo)
    }

    data class AddShippingInfoParams(
        val cartId: String?,
        val userId: String?,
        val shippingInfo: ShippingInfoEntity,
    ) : UseCase.Params() {

        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId),
                WardCodeValidator(
                    obj = shippingInfo.wardCode,
                    fieldName = "wardCode",
                    minLength = CODE_MIN_LENGTH,
                    maxLength = CODE_MAX_LENGTH,
                    required = false
                ),
                StringValidator(
                    obj = shippingInfo.districtCode,
                    fieldName = "districtCode",
                    minLength = CODE_MIN_LENGTH,
                    maxLength = CODE_MAX_LENGTH
                ),
                StringValidator(
                    obj = shippingInfo.provinceCode,
                    fieldName = "provinceCode",
                    minLength = CODE_MIN_LENGTH,
                    maxLength = CODE_MAX_LENGTH
                ),
                StringValidator(
                    obj = shippingInfo.wardName,
                    fieldName = "wardName",
                    minLength = STRING_MIN_LENGTH,
                    maxLength = STRING_MAX_LENGTH,
                    required = false
                ),
                StringValidator(
                    obj = shippingInfo.districtName,
                    fieldName = "districtName",
                    minLength = STRING_MIN_LENGTH,
                    maxLength = STRING_MAX_LENGTH
                ),
                StringValidator(
                    obj = shippingInfo.provinceName,
                    fieldName = "provinceName",
                    minLength = STRING_MIN_LENGTH,
                    maxLength = STRING_MAX_LENGTH
                ),
                StringValidator(
                    obj = shippingInfo.address,
                    fieldName = "address",
                    minLength = STRING_MIN_LENGTH,
                    maxLength = STRING_MAX_LENGTH
                ),
                StringValidator(
                    obj = shippingInfo.addressNote,
                    fieldName = "addressNote",
                    minLength = STRING_MIN_LENGTH,
                    maxLength = STRING_MAX_LENGTH,
                    required = false
                ),
                StringValidator(
                    obj = shippingInfo.name,
                    fieldName = "name",
                    minLength = NAME_MIN_LENGTH,
                    maxLength = STRING_MAX_LENGTH
                ),
                EmailValidator(email = shippingInfo.email),
                PhoneNumberValidator(phone = shippingInfo.telephone, required = true)
            )

    }
}
