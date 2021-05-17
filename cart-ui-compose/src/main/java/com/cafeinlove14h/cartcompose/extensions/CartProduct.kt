package com.cafeinlove14h.cartcompose.extensions

import android.content.Context
import com.cafeinlove14h.cartcompose.R
import vn.teko.cart.android.bus.model.CartProduct
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.CartPromotionEntity

fun CartProduct.getVariantsValue(): String? {
    return productDetail?.productGroup?.variants?.flatMap { it.attributeValues }
        ?.joinToString(" | ") { it.value }
}

fun CartProduct.getSupplierRetailPrice(): Long {
    return prices.firstOrNull()?.supplierRetailPrice ?: 0L
}

fun CartProduct.getStockLabel(context: Context): String {
    return context.getString(if (totalAvailable == null && totalAvailable == 0) R.string.cart_ui_out_of_stock else R.string.cart_ui_available_for_sale)
}

fun CartProduct.getAppliedPromotions(
    promotions: List<CartPromotionEntity>,
    cartItem: CartItemEntity
) = this.promotions.filter {
    promotions.any { cartPromo ->
        cartPromo.promotionId == it.id && cartPromo.applyOn.size == 1 && cartPromo.applyOn.first().lineItemId == cartItem.lineItemId
    }
}