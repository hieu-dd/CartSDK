package com.cafeinlove14h.cartcompose.extensions

import androidx.annotation.StringRes
import com.cafeinlove14h.cartcompose.R
import vn.teko.cart.core.infrastructure.cart.error.CartError
import vn.teko.cart.domain.exception.CartError.CheckoutError
import vn.teko.cart.domain.exception.ExternalServiceError

@StringRes
fun CartError.getHelpMessageIdFromError(): Int {
    return when (this.code) {
        ExternalServiceError.PpmError.UNSPECIFIED_ERROR -> R.string.cart_ui_common_internal_server_error
        ExternalServiceError.PpmError.GET_PROMOTION_INVALID_GRAND_TOTAL -> R.string.cart_ui_promotion_invalid_grand_total
        ExternalServiceError.PpmError.GET_PROMOTION_COUPON_OUT_OF_STOCK -> R.string.cart_ui_promotion_coupon_out_of_stock
        ExternalServiceError.PpmError.GET_PROMOTION_INVALID_SKU -> R.string.cart_ui_promotion_invalid_sku
        ExternalServiceError.PpmError.GET_PROMOTION_COUPON_INVALID_OR_HAS_BEEN_USED -> R.string.cart_ui_promotion_coupon_invalid_or_has_been_used
        ExternalServiceError.PpmError.GET_PROMOTION_INVALID_USER -> R.string.cart_ui_promotion_invalid_user
        ExternalServiceError.PpmError.GET_PROMOTION_COUPON_INVALID -> R.string.cart_ui_promotion_coupon_invalid
        ExternalServiceError.PpmError.GET_PROMOTION_ERROR -> R.string.cart_ui_promotion_error
        ExternalServiceError.PpmError.GET_PROMOTION_OUT_OF_BUDGET -> R.string.cart_ui_promotion_out_of_budget
        ExternalServiceError.PpmError.GET_PROMOTION_EXCEED_MAX_USAGE_PER_USER -> R.string.cart_ui_promotion_exceed_max_usage_per_user
        ExternalServiceError.PpmError.GET_PROMOTION_USER_NEED_LOGIN -> R.string.cart_ui_promotion_user_need_login
        ExternalServiceError.PpmError.GET_COUPON_PROMOTION_ERROR, ExternalServiceError.PpmError.GET_ORDER_PROMOTIONS_ERROR -> R.string.cart_ui_coupon_promotions_invalid

        CheckoutError.CONFIRM_VOUCHER_OUT_OF_BUDGET -> R.string.cart_ui_confirm_voucher_out_of_budget
        CheckoutError.CONFIRM_BUDGET_OUT_OF_BUDGET -> R.string.cart_ui_confirm_budget_out_of_budget
        CheckoutError.CONFIRM_PROMOTION_COUPON_USED_OR_OUT_OF_BUDGET -> R.string.cart_ui_confirm_coupon_used_or_out_of_budget
        CheckoutError.CONFIRM_ORDER_PROMOTION_INVALID_PRICE -> R.string.cart_ui_confirm_order_promotion_invalid_price
        CheckoutError.CONFIRM_ORDER_PROMOTION_EXCEED_MAX_USAGE_PER_USER -> R.string.cart_ui_confirm_order_promotion_exceed_max_usage_per_user
        CheckoutError.CONFIRM_ORDER_PROMOTION_PROMOTION_INACTIVE -> R.string.cart_ui_confirm_order_promotion_promotion_inactive
        CheckoutError.CONFIRM_ORDER_PROMOTION_INVALID_PROMOTION_SKU -> R.string.cart_ui_confirm_order_promotion_invalid_promotion_sku
        CheckoutError.CONFIRM_ORDER_PROMOTION_INVALID_PROMOTION_COUPON_USER -> R.string.cart_ui_confirm_order_promotion_invalid_promotion_coupon_user
        CheckoutError.CONFIRM_ORDER_PROMOTION_COUPON_EXPIRED -> R.string.cart_ui_confirm_order_promotion_coupon_expired
        CheckoutError.CONFIRM_FLASH_SALE_INACTIVE -> R.string.cart_ui_confirm_flash_sale_inactive
        CheckoutError.CONFIRM_FLASH_SALE_OUT_OF_STOCK -> R.string.cart_ui_confirm_flash_sale_out_of_stock
        else -> R.string.cart_common_error_occur_popup
    }
}
