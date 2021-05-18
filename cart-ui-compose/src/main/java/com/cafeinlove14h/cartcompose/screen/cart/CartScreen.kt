package com.cafeinlove14h.cartcompose.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.cafeinlove14h.cartcompose.screen.cart.components.CartLineItemView
import com.cafeinlove14h.cartcompose.screen.cart.components.CartPromotionItemView
import com.cafeinlove14h.cartcompose.screen.cart.components.Header
import vn.teko.cart.core.infrastructure.cart.data.DataState
import vn.teko.cart.domain.model.CartEntity

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltNavGraphViewModel()
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF8F8FC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val cartDataState by viewModel.cartFlow.collectAsState()

            Header(title = "Cart")
            cartDataState.data?.let {
                CartList(cart = it)
            }
        }
    }
}

@Composable
fun CartList(cart: CartEntity, viewModel: CartViewModel = hiltNavGraphViewModel()) {
    val lineItems = cart.items
    val promotion = cart.promotions
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        items(items = lineItems, key = { it.lineItemId }) {
            CartLineItemView(lineItem = it) { id, quantity ->
                viewModel.updateItem(id, quantity)
            }
        }
        items(items = promotion, key = { it.id }) {
            CartPromotionItemView(promotion = it)
        }
    }
}