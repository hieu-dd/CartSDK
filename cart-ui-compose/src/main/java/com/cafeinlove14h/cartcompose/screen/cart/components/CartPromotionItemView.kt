package com.cafeinlove14h.cartcompose.screen.cart.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import vn.teko.cart.domain.model.CartPromotionEntity

@Composable
fun CartPromotionItemView(promotion: CartPromotionEntity) {
    Text(text = promotion.base?.description.orEmpty(), style = MaterialTheme.typography.caption)
}