package com.cafeinlove14h.cartwithcompose.ui.screen.cart

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.cafeinlove14h.cartwithcompose.model.CartItemEntity

@Composable
fun CartLineItemView(item: CartItemEntity) {
    Row() {
        Text(text = item.name)
    }
}