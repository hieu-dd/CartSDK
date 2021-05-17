package com.cafeinlove14h.cartcompose.extensions

import vn.teko.cart.android.bus.model.CartProduct
import vn.teko.cart.domain.model.CartItemEntity

fun CartItemEntity.getDiscountPercent(product: CartProduct): Long {
    val supplierRetailPrice = product.getSupplierRetailPrice()
    return if (supplierRetailPrice > price) 100 - 100 * price / supplierRetailPrice else 0
}