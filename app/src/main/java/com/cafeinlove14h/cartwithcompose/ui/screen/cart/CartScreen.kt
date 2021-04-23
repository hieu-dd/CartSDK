package com.cafeinlove14h.cartwithcompose.ui.screen.cart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cafeinlove14h.cartwithcompose.MainViewModel
import com.cafeinlove14h.cartwithcompose.model.CartEntity

@Composable
fun CartScreen(mainViewModel: MainViewModel) {
    val cart: CartEntity? by mainViewModel.cart.collectAsState()
    LazyColumn(
        modifier = Modifier.height(100.dp).fillMaxWidth()
    ) {
        items(cart?.items.orEmpty()) { item ->
            CartLineItemView(item)
        }
    }
}