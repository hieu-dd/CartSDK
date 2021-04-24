package vn.teko.cart.domain.exception

import vn.teko.cart.domain.exception.Error.FeatureError


/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific error should extend [FeatureError] class.
 */
sealed class Error(
    val code: Int,
    override val message: String,
    override val cause: Throwable?
) : Throwable(message, cause) {

    companion object {
        const val UNEXPECTED_ERROR_STATUS_CODE = 500_000
        const val NOT_FOUND_ERROR_STATUS_CODE = 404_000
        const val UNAUTHORIZED_ERROR_STATUS_CODE = 401_000
        const val SERIALIZATION_ERROR_STATUS_CODE = 400_000
    }

    /**
     * Class for unexpected error
     */
    class UnexpectedError(cause: Throwable?) : Error(
        code = UNEXPECTED_ERROR_STATUS_CODE,
        message = "An unexpected error occurred",
        cause = cause
    )

    class NotFoundError(cause: Throwable?) : Error(
        code = NOT_FOUND_ERROR_STATUS_CODE,
        message = "Not found",
        cause = cause
    )

    object UnauthorizedError : Error(
        code = UNAUTHORIZED_ERROR_STATUS_CODE,
        message = "Unauthorized",
        cause = null
    )

    class SerializationError(message: String, cause: Throwable?) : Error(
        code = SERIALIZATION_ERROR_STATUS_CODE,
        message = message,
        cause = cause
    )

    /**
     * Extend this class for feature specific errors.
     * The error code must strictly following the rule: <HTTP Status Code>_<Feature Code> for further investigation
     * The error code must be unique, each feature should have a given range for freely internal usage
     * @sample: 400_xxx means the return status code is 400 while the investigation code is xxx
     */
    abstract class FeatureError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : Error(code, message, cause)
}

/**
 * Exception on dealing with Cart Identity
 * Error code range: [001-099]
 */
sealed class CartIdentityError(
    code: Int,
    message: String,
    cause: Throwable?
) : Error.FeatureError(code, message, cause) {

    /**
     *  Exception may encounter when generating cart token
     *  Error code range: [001-009]
     */
    companion object {
        const val TOKEN_VERIFICATION_FAILED_STATUS_CODE = 400_010
    }

    sealed class GenerateCartToken(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartIdentityError(code, message, cause) {
        companion object {
            const val GENERATE_CART_TOKEN_ERROR_STATUS_CODE = 400_001
        }

        /** In case params provided are invalid */
        object InvalidParams : CartIdentityError.GenerateCartToken(
            GENERATE_CART_TOKEN_ERROR_STATUS_CODE,
            "Invalid Generate Cart Token Params",
            null
        )
    }

    /** Exception may encounter when verify cart token */
    object FailedTokenVerification :
        CartIdentityError(TOKEN_VERIFICATION_FAILED_STATUS_CODE, "Failed to verify Cart Token", null)
}

/**
 * Exception on interacting with User Cart
 * Error code range: [101-199], [901, 999]
 */
sealed class CartError(
    code: Int,
    message: String,
    cause: Throwable?
) : Error.FeatureError(code, message, cause) {

    /**
     * Exception when cannot identify current cart
     * Error code range: [101-109], [901, 999]
     */
    companion object {
        const val CART_NOT_FOUND_STATUS_CODE = 404_101
        const val MISSING_TERMINAL_STATUS_CODE = 400_102
        const val MISSING_CHANNEL_STATUS_CODE = 400_103
        const val UNSPECIFIED_TENANT_STATUS_CODE = 400_104
        const val INVALID_CART_ID_STATUS_CODE = 400_105
        const val INVALID_CART_TOKEN_STATUS_CODE = 400_106
        const val MISSING_CART_INFO_STATUS_CODE = 400_107
        const val EXCEED_MAX_QUANTITY_STATUS_CODE = 400_108
        const val PRODUCT_NOT_FOUND_STATUS_CODE = 400_109
        const val CART_ITEM_NOT_FOUND_STATUS_CODE = 404_901
        const val OUT_OF_STOCK_STATUS_CODE = 400_902
        const val INVALID_CART_ITEM_ID_STATUS_CODE = 400_903
        const val INVALID_EMAIL_STATUS_CODE = 400_904
        const val INVALID_TELEPHONE_STATUS_CODE = 400_905
        const val NOT_ENOUGH_STOCK_STATUS_CODE = 400_906
    }

    /** In case we cannot find cart with given identifiers */
    object CartNotFound : CartError(CART_NOT_FOUND_STATUS_CODE, "Cart not found", null)

    /** In case the provided terminal identifier is missing*/
    object MissingTerminal : CartError(MISSING_TERMINAL_STATUS_CODE, "Missing terminal", null)

    /** In case the provided channel identifier is missing */
    object MissingChannel : CartError(MISSING_CHANNEL_STATUS_CODE, "Missing channel", null)

    /** In case the provided tenant identifier is missing */
    object UnspecifiedTenant : CartError(UNSPECIFIED_TENANT_STATUS_CODE, "Unspecified tenant", null)

    /** In case provided cart identifier is invalid */
    object InvalidCartId : CartError(INVALID_CART_ID_STATUS_CODE, "Invalid cart id", null)

    /** In case provided cart identifier is invalid */
    object InvalidCartToken : CartError(INVALID_CART_TOKEN_STATUS_CODE, "invalid cart token", null)

    /** In case provided cart identifier is invalid */
    object MissingCartInfo : CartError(MISSING_CART_INFO_STATUS_CODE, "Missing cart id or user token", null)

    /** In case the selected quantity exceeded maximum cart product quantity */
    object ExceedMaxQuantity :
        CartError(EXCEED_MAX_QUANTITY_STATUS_CODE, "Exceeded maximum cart product quantity", null)

    /** In case the sku params is not available from Discovery response  */
    class ProductNotFound(val sku: String) :
        CartError(PRODUCT_NOT_FOUND_STATUS_CODE, "Product $sku is not available", null)

    /** In case the lineItem params is not found in current cart  */
    class CartItemNotFound(cartItemId: String?) : CartError(
        CART_ITEM_NOT_FOUND_STATUS_CODE,
        "Cart Item ${cartItemId ?: ""} is not found or null".replace("\\s+", " "),
        null
    )

    /** In case the selected quantity exceeded total available cart product quantity */
    object OutOfStock : CartError(OUT_OF_STOCK_STATUS_CODE, "Out of stock quantity, item is removed from cart", null)

    object NotEnoughStockQuantity : CartError(NOT_ENOUGH_STOCK_STATUS_CODE, "Stock quantity is not enough", null)

    /** In case the item line identifier is invalid (not an uuid) */
    class InvalidCartItemId(cartItemId: String?) :
        CartError(INVALID_CART_ITEM_ID_STATUS_CODE, "Invalid line item id: $cartItemId", null)

    /** In case the email is invalid */
    object InvalidEmail : CartError(INVALID_EMAIL_STATUS_CODE, "Email is invalid", null)

    /** In case the telephone is invalid */
    object InvalidTelephone : CartError(INVALID_TELEPHONE_STATUS_CODE, "Telephone is invalid", null)

    /**
     * Exception when adding product to cart
     * Error code range: [111-119]
     */
    sealed class AddToCartError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartError(code, message, cause) {
        companion object {
            const val NON_EXISTENT_PROMOTION_STATUS_CODE = 400_111
            const val INAPPLICABLE_PROMOTION = 400_112
        }

        /** In case the selected promotion is not found */
        object NonExistentPromotion : CartError(
            NON_EXISTENT_PROMOTION_STATUS_CODE,
            "The selected promotion does not appear in product info",
            null
        )

        /** In case user choose a promotion, but it is not available in the product data */
        object InapplicablePromotion :
            CartError(INAPPLICABLE_PROMOTION, "The selected promotion is not applicable", null)
    }


    /**
     * Exception when cannot identify current cart
     * Error code range: [121-129]
     */
    sealed class RemoveLineItemError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartError(code, message, cause)


    /**
     * Exception when unable to update an item
     * Error code range: [131-139]
     */
    sealed class UpdateCartItemError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartError(code, message, cause) {
        companion object {
            const val INVALID_PARAMS_STATUS_CODE = 400_133
        }

        /** In case the provided params are invalid */
        object InvalidParams :
            UpdateCartItemError(INVALID_PARAMS_STATUS_CODE, "Both selected and quality are null", null)
    }

    /**
     * Exception when cannot add shipping info into cart
     * Error code range: [141-149]
     */
    sealed class AddShippingInfoError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartError(code, message, cause)

    /**
     * Exception when unable to update items
     * Error code range: [161-169]
     */
    sealed class UpdateCartItemsBySellerError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartError(code, message, cause) {
        companion object {
            const val INVALID_SELLER_ID = 400_161
            const val MISSING_SELLER_ID = 400_162
        }

        /** In case the provided seller id is invalid */
        object InvalidSellerId : UpdateCartItemsBySellerError(INVALID_SELLER_ID, "Invalid seller id", null)

        /** In case the provided seller id is missing */
        object MissingSellerId : UpdateCartItemsBySellerError(MISSING_SELLER_ID, "Missing seller id", null)
    }

    /**
     * Exception when checkout payload is invalid or missing
     * Error code range: [150 - 153]
     */

    sealed class CheckoutError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartError(code, message, cause) {

        companion object {
            const val NO_ITEMS_TO_CHECKOUT = 400_150
            const val INVALID_SHIPPING_LOCATION = 400_151
            const val SHIPPING_INFO_REQUIRED = 400_152
            const val INVALID_PRODUCT = 400_153
            const val DELIVERY_SERVICE_REQUIRED = 400_154
        }

        object NoItemsToCheckout : CheckoutError(NO_ITEMS_TO_CHECKOUT, "Cart is empty or no selected items", null)
        object InvalidShippingLocation : CheckoutError(INVALID_SHIPPING_LOCATION, "Invalid shipping location", null)
        object ShippingInfoRequired : CheckoutError(SHIPPING_INFO_REQUIRED, "Shipping info required", null)
        object InvalidProduct : CheckoutError(INVALID_PRODUCT, "Some invalid products are in the current cart", null)
        object DeliveryServiceRequired : CheckoutError(DELIVERY_SERVICE_REQUIRED, "Delivery service required", null)
    }

    /**
     * Exception when unable to update items
     * Error code range: [171-179]
     */

    sealed class GetPaymentMethodsError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : CartError(code, message, cause) {
        companion object {
            const val ORDER_NOT_FOUND = 404_171
            const val CANNOT_GET_ORDER = 400_172
            const val CANNOT_GET_CART = 400_173
            const val ORDER_CANNOT_BE_REACHED = 500_174
        }

        /** In case the order cannot be found */
        object OrderNotFound : GetPaymentMethodsError(ORDER_NOT_FOUND, "Order not found", null)

        /** In case we cannot get the order */
        object CannotGetOrder : GetPaymentMethodsError(CANNOT_GET_ORDER, "Failed to get order detail", null)

        /** In case we cannot get the cart information */
        object CannotGetCart : GetPaymentMethodsError(CANNOT_GET_CART, "Failed to get the current cart", null)

        /** In case we cannot reach the order server */
        object OrderCannotBeReached : GetPaymentMethodsError(ORDER_CANNOT_BE_REACHED, "Unable to connect to OC", null)
    }
}

/**
 * Exception on interacting with Promotion
 * Error code range: [201-299]
 */
sealed class PromotionError(
    code: Int,
    message: String,
    cause: Throwable?
) : Error.FeatureError(code, message, cause) {

    /**
     * Exception when cannot apply order coupon
     * Error code range: [201-209]
     */
    sealed class ApplyCouponError(
        code: Int,
        message: String,
        cause: Throwable?,
    ) : PromotionError(code, message, cause) {
        companion object {
            const val INVALID_COUPON_STATUS_CODE = 400_201
        }

        /** In case coupon is not applicable, whether there is no promotion or the condition is not met */
        data class InvalidCoupon(
            val coupon: String,
        ) : ApplyCouponError(INVALID_COUPON_STATUS_CODE, "Coupon $coupon is not applicable", null)
    }

}

/**
 * Exception when validate data
 * Error code range: [301-399]
 */

sealed class ValidationDataError(
    code: Int, message: String, cause: Throwable?
) : Error.FeatureError(code, message, cause) {

    companion object {
        const val FIELD_EXCEED_MAX_LENGTH_STATUS_CODE = 400_301
        const val FIELD_EXCEED_MAX_VALUE_STATUS_CODE = 400_302
        const val FIELD_NUMBER_OUT_OF_RANGE = 400_303
        const val FIELD_IS_INVALID = 400_304
        const val FIELD_LENGTH_ERROR_STATUS_CODE = 400_305
    }

    /** In case field is exceeding of max length */
    data class FieldExceedingMaxLength(val fieldName: String) :
        ValidationDataError(FIELD_EXCEED_MAX_LENGTH_STATUS_CODE, "Field $fieldName is exceeding the max length", null)

    /** In case field is exceeding of max value */
    data class FieldExceedingMaxValue(val fieldName: String) :
        ValidationDataError(FIELD_EXCEED_MAX_VALUE_STATUS_CODE, "Field $fieldName is exceeding the max value", null)

    /** In case field is not valid */
    data class FieldNumberOutOfRange(val fieldName: String, val minimum: String, val maximum: String) :
        ValidationDataError(FIELD_NUMBER_OUT_OF_RANGE, "Field $fieldName is out of range [$minimum, $maximum]", null)

    /** In case field is null or empty while it should not */
    data class FieldIsInvalid(val fieldName: String) :
        ValidationDataError(
            FIELD_IS_INVALID,
            "Field $fieldName is invalid (wrong format or null/empty while it should not)",
            null
        )

    /** In case string field's length is out of Range */
    data class FieldLengthOutOfRange(val fieldName: String, val minLength: String, val maxLength: String) :
        ValidationDataError(
            FIELD_LENGTH_ERROR_STATUS_CODE,
            "Field ${fieldName}'s length is out of range [$minLength, $maxLength]",
            null
        )
}

/**
 * Exception on external service
 */

sealed class ExternalServiceError(
    code: Int,
    message: String,
    cause: Throwable?
) : Error.FeatureError(code, message, cause) {

    /**
     * Exception when error from discovery
     * Error code range: [001-009]
     */
    sealed class DiscoveryError(
        code: Int,
        message: String,
        cause: Throwable?,
    ) : ExternalServiceError(code, message, cause) {
        companion object {
            const val UNSPECIFIED_ERROR = 500_001
            const val GET_PRODUCTS_ERROR = 400_002
        }

        object UnspecifiedError : DiscoveryError(UNSPECIFIED_ERROR, "[Discovery] Unable to connect to Discovery", null)

        /** In case the target get product of cart from discovery error */
        data class GetProductsError(
            val error: String? = null,
        ) : DiscoveryError(GET_PRODUCTS_ERROR, error ?: "[Discovery] Get Products Error", null)
    }

    /**
     * Exception when error from ppm
     * Error code range: [010-019]
     */
    sealed class PpmError(
        code: Int,
        message: String,
        cause: Throwable?,
    ) : ExternalServiceError(code, message, cause) {
        companion object {
            const val UNSPECIFIED_ERROR = 500_010
            const val GET_COUPON_PROMOTION_ERROR = 400_011
            const val GET_ORDER_PROMOTIONS_ERROR = 400_012
        }

        object UnspecifiedError : PpmError(UNSPECIFIED_ERROR, "[PPM] Unable to connect to PPM", null)

        /** In case the target get coupon promotion error */
        data class GetCouponPromotionError(
            val error: String? = null
        ) : PpmError(GET_COUPON_PROMOTION_ERROR, error ?: "[PPM] Get Coupon Error", null)

        /** In case the target get order promotions error */
        data class GetOrderPromotionsError(
            val error: String? = null
        ) : PpmError(GET_ORDER_PROMOTIONS_ERROR, error ?: "[PPM] Get Order Promotions Error", null)
    }

    /**
     * Exception when error from policies
     * Error code range: [020-29]
     */
    sealed class PoliciesError(
        code: Int,
        message: String,
        cause: Throwable?,
    ) : ExternalServiceError(code, message, cause) {
        companion object {
            const val UNSPECIFIED_ERROR = 500_020
            const val GET_POLICIES_ERROR = 400_021
        }

        object UnspecifiedError : PoliciesError(UNSPECIFIED_ERROR, "[POLICIES] Unable to connect to POLICIES", null)

        /** In case the target get shipping fee error */
        data class GetPoliciesError(
            val error: String? = null,
        ) : PoliciesError(GET_POLICIES_ERROR, error ?: "[POLICIES] Get P Error", null)
    }

    /**
     * Exception when error from order capture
     * Error code range: [030 - 034]
     */
    sealed class OrderCaptureError(code: Int, message: String, cause: Throwable?) :
        ExternalServiceError(code, message, cause) {
        companion object {
            const val CREATE_ORDER_ERROR = 500_030
            const val SERIALIZE_ORDER = 500_031
            const val ORDER_NOT_FOUND = 404_032
            const val CANNOT_GET_ORDER = 500_033
            const val UNSPECIFIED_ERROR = 500_034
        }

        data class CreateOrderError(val error: String? = null) :
            OrderCaptureError(CREATE_ORDER_ERROR, "[ORDER CAPTURE] $error", null)

        /** In case of cannot find order */
        object OrderNotFound
            : OrderCaptureError(ORDER_NOT_FOUND, "[ORDER CAPTURE] Order Not Found", null)

        object CannotGetOrder
            : OrderCaptureError(CANNOT_GET_ORDER, "[ORDER CAPTURE] Can not get order", null)

        object SerializeOrderError
            : OrderCaptureError(SERIALIZE_ORDER, "Can not serialize order", null)

        object UnspecifiedError
            : OrderCaptureError(UNSPECIFIED_ERROR, "[ORDER CAPTURE] Unable to connect to OC", null)
    }

    /**
     * Exception when error from PS
     * Error code range: [040-049]
     */
    sealed class PsError(code: Int, message: String, cause: Throwable?) :
        ExternalServiceError(code, message, cause) {
        companion object {
            const val CANNOT_GET_PAYMENT_METHODS = 500_040
            const val UNSPECIFIED_ERROR = 500_041
        }

        object GetPaymentMethodsError : PsError(CANNOT_GET_PAYMENT_METHODS, "[Ps] Can not get payment methods", null)

        object UnspecifiedError
            : PsError(UNSPECIFIED_ERROR, "[Ps] Failed to get payments", null)
    }

    /*
    * Exception when error from loyalty
    * Error code range" [050-059]
    * */
    sealed class LoyaltyError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : ExternalServiceError(code, message, cause) {
        companion object {
            const val CANNOT_CALL_LOYALTY = 400_050
            const val UNSPECIFIED_ERROR = 500_051
        }

        data class PreCalculatePointError(val error: String?) :
            LoyaltyError(CANNOT_CALL_LOYALTY, error ?: "[Loyalty] Calling loyalty error", null)

        object UnspecifiedError : LoyaltyError(UNSPECIFIED_ERROR, "[Loyalty] Failed to connect loyalty", null)
    }

    /*
    * Exception when error from policy v2
    * Error code range" [060-069]
    * */
    sealed class ServiceError(
        code: Int,
        message: String,
        cause: Throwable?
    ) : ExternalServiceError(code, message, cause) {
        companion object {
            const val GET_SERVICE_ERROR = 400_060
            const val UNSPECIFIED_ERROR = 500_061
            const val SHIPPING_INFO_REQUIRED = 400_062
        }

        data class GetServiceError(val error: String? = null) :
            ServiceError(GET_SERVICE_ERROR, error ?: "[Service] Calling to policy v2 error", null)

        object UnspecifiedError : ServiceError(UNSPECIFIED_ERROR, "[Service] Failed to connect policy v2", null)

        object ShippingInfoRequired : ServiceError(SHIPPING_INFO_REQUIRED, "shipping info is required", null)

    }



}
