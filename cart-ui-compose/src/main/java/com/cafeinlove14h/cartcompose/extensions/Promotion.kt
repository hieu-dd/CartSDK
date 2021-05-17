package com.cafeinlove14h.cartcompose.extensions

import android.annotation.SuppressLint
import android.content.Context
import com.cafeinlove14h.cartcompose.R
import vn.teko.cart.android.bus.model.Promotion
import com.cafeinlove14h.cartcompose.utils.CurrencyUtils.formatMoney

@SuppressLint("StringFormatMatches")
fun Promotion.Benefit.Discount.getBenefitDescription(context: Context): String {
    flat?.let {
        if (it > 0) return context.getString(R.string.cart_ui_item_discount_flat, formatMoney(it))
    }
    return percent?.let { percent ->
        when (maxAmount) {
            null -> context.getString(
                R.string.cart_ui_item_discount_percent_without_max_amount,
                "${percent.toInt()}%"
            )
            else -> context.getString(
                R.string.cart_ui_item_discount_percent,
                "${percent.toInt()}%",
                formatMoney(maxAmount!!)
            )
        }
    }.orEmpty()
}

fun Promotion.Benefit.Voucher.getBenefitDescription(context: Context) =
    context.getString(R.string.cart_ui_item_voucher, this.quantity)

fun Promotion.Benefit.Gift.getBenefitDescription(context: Context) =
    context.getString(R.string.cart_ui_item_gift, this.quantity, this.name)





