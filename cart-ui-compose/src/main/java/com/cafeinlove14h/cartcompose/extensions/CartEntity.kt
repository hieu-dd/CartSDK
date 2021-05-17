package com.cafeinlove14h.cartcompose.extensions

import vn.teko.cart.domain.model.CartEntity

fun CartEntity.getProductItems() = this.items.filter { !it.isGift }
