package com.cafeinlove14h.cartwithcompose.ui.screen.cart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cafeinlove14h.cartwithcompose.MainViewModel
import vn.teko.cart.core.infrastructure.cart.data.DataState
import vn.teko.cart.domain.model.CartEntity

@Composable
fun CartScreen(mainViewModel: MainViewModel) {
    val cartDataState: DataState<*> by mainViewModel.cart.collectAsState(DataState.Idle)
    when (cartDataState) {
        is DataState.Loading -> {
            CircularProgressIndicator(progress = 0.5f)
        }
        is DataState.Success<*> -> {
            val cart = (cartDataState as DataState.Success<*>).data as CartEntity
            LazyColumn(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            ) {
                items(cart.items) { item ->
                    CartLineItemView(item)
                }
            }
        }
    }

}