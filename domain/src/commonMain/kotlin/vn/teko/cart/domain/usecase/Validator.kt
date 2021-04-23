package vn.teko.cart.domain.usecase

import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.exception.Error
import vn.teko.cart.domain.exception.ValidationDataError
import vn.teko.cart.domain.util.isUUID

abstract class Validator {
    companion object {
        const val CODE_MAX_LENGTH = 6
        const val CODE_MIN_LENGTH = 2
        const val STRING_MIN_LENGTH = 0
        const val STRING_MAX_LENGTH = 255
        const val EMAIL_MAX_LENGTH = 254
        const val TELEPHONE_MIN_LENGTH = 10
        const val TELEPHONE_MAX_LENGTH = 12
        const val INVOICE_NAME_MAX_LENGTH = 255
        const val INVOICE_TAX_CODE_MAX_LENGTH = 32
        const val INVOICE_ADDRESS_MAX_LENGTH = 255
        const val NOTE_MAX_LENGTH = 255
        const val NAME_MIN_LENGTH = 1
    }

    open fun validate(): Error? {
        return null
    }
}

open class CartValidator(val cartId: String?, val userId: String?) : Validator() {
    override fun validate(): Error? = super.validate() ?: when {
        cartId != null && !cartId.isUUID() -> CartError.InvalidCartId
        cartId == null && userId.isNullOrBlank() -> CartError.MissingCartInfo
        else -> null
    }
}

open class CartItemValidator(private val cartItemId: String) : Validator() {
    override fun validate(): Error? = super.validate() ?: when {
        !cartItemId.isUUID() -> CartError.InvalidCartItemId(cartItemId)
        else -> null
    }
}

/**
 *
 */
open class NumberValidator<T : Comparable<*>?>(
    private val obj: T?,
    private val fieldName: String,
    private val minimum: T?,
    private val maximum: T?,
    private val required: Boolean = false
) : Validator() {
    companion object {
        const val MIN_ID_VALUE = 1
    }

    override fun validate(): Error? = super.validate() ?: when {
        obj == null && required ->
            ValidationDataError.FieldIsInvalid(fieldName)
        obj != null && minimum != null && compareValues(obj, minimum) < 0 ->
            ValidationDataError.FieldNumberOutOfRange(
                fieldName = fieldName,
                minimum = minimum.toString(),
                maximum = maximum.toString()
            )
        obj != null && maximum != null && compareValues(obj, maximum) > 0 ->
            ValidationDataError.FieldNumberOutOfRange(
                fieldName = fieldName,
                minimum = minimum.toString(),
                maximum = maximum.toString()
            )
        else -> null
    }
}


open class StringValidator(
    private val obj: String?,
    private val fieldName: String,
    private val minLength: Int? = 0,
    private val maxLength: Int? = Int.MAX_VALUE,
    private val required: Boolean = true
) : Validator() {
    override fun validate(): Error? = super.validate() ?: when {
        obj.isNullOrBlank() && required -> ValidationDataError.FieldIsInvalid(fieldName)
        obj == "null" -> ValidationDataError.FieldIsInvalid(fieldName)
        obj != null && minLength != null && obj.length < minLength ->
            ValidationDataError.FieldLengthOutOfRange(fieldName, minLength.toString(), maxLength.toString())
        obj != null && maxLength != null && obj.length > maxLength ->
            ValidationDataError.FieldLengthOutOfRange(fieldName, minLength.toString(), maxLength.toString())
        else -> null
    }
}

@Deprecated("Only for backward compatible, will be removed")
open class WardCodeValidator(
    private val obj: String?,
    private val fieldName: String,
    private val minLength: Int? = 0,
    private val maxLength: Int? = Int.MAX_VALUE,
    private val required: Boolean = true
) : Validator() {
    override fun validate(): Error? = super.validate() ?: when {
        obj.isNullOrBlank() && required -> ValidationDataError.FieldIsInvalid(fieldName)
        obj == "null" -> ValidationDataError.FieldIsInvalid(fieldName)
        !obj.isNullOrEmpty() && minLength != null && obj.length < minLength ->
            ValidationDataError.FieldLengthOutOfRange(fieldName, minLength.toString(), maxLength.toString())
        !obj.isNullOrEmpty() && maxLength != null && obj.length > maxLength ->
            ValidationDataError.FieldLengthOutOfRange(fieldName, minLength.toString(), maxLength.toString())
        else -> null
    }
}


class EmailValidator(private val email: String?, required: Boolean = false) :
    StringValidator(email, "email", STRING_MIN_LENGTH, EMAIL_MAX_LENGTH, required) {
    companion object {
        const val EMAIL_REGEX = (
                "^[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+\$"
                )
    }

    private val matcher = EMAIL_REGEX.toRegex()

    override fun validate(): Error? = super.validate() ?: when {
        email != null && !matcher.matches(email) -> CartError.InvalidEmail
        else -> null
    }
}


class PhoneNumberValidator(private val phone: String?, required: Boolean = false) : StringValidator(
    phone, "telephone",
    TELEPHONE_MIN_LENGTH,
    TELEPHONE_MAX_LENGTH,
    required
) {
    companion object {
        // // sdd = space, dot, or dash
        const val PHONE_NUMBER_REGEX = ("(\\+[0-9]+[\\- \\.]*)?"  // +<digits><sdd>*
                + "(\\([0-9]+\\)[\\- \\.]*)?"  // (<digits>)<sdd>*
                + "([0-9][0-9\\- \\.]+[0-9])") // <digit><digit|sdd>+<digit>
    }

    private val matcher = PHONE_NUMBER_REGEX.toRegex()

    override fun validate(): Error? = super.validate() ?: when {
        phone != null && !matcher.matches(phone) -> CartError.InvalidTelephone
        else -> null
    }
}