package com.cafeinlove14h.cartwithcompose.ui.screen.productlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cafeinlove14h.cartwithcompose.model.Product

@ExperimentalFoundationApi
@Composable
fun ProductListScreen(viewModel: ProductListViewModel) {
    val products: List<Product> by viewModel.products.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(
            text = "All Products",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .height(40.dp)
                .background(Color.White)
        )
        LazyVerticalGrid(
            cells = GridCells.Fixed(2)
        ) {
            items(products) { product ->
                ProductItem(product = product) {
                    viewModel.addItem(it)
                }
            }
        }
    }
}

