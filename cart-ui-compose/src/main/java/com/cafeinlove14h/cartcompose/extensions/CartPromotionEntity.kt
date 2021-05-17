package com.cafeinlove14h.cartcompose.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.cafeinlove14h.cartcompose.R
import com.cafeinlove14h.cartcompose.utils.CurrencyUtils.formatMoney
import vn.teko.cart.domain.model.CartPromotionEntity

fun CartPromotionEntity.getDescription(context: Context): String {
    val descriptions = mutableListOf<String>()
    if (discount > 0.0) {
        descriptions.add(
            context.getString(R.string.cart_ui_cart_promotion_discount, formatMoney(discount))
        )
    }

    if (gifts.isNotEmpty()) {
        descriptions.add(
            context.getString(
                R.string.cart_ui_cart_promotion_gift,
                gifts.joinToString(", ") { it.name })
        )
    }

    voucher?.let {
        descriptions.add(context.getString(R.string.cart_ui_cart_promotion_voucher))
    }

    return descriptions.joinToString(" - ")
}