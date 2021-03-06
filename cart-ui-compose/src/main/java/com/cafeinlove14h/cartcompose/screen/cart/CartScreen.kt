package com.cafeinlove14h.cartcompose.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import com.cafeinlove14h.cartcompose.CartSdk
import com.cafeinlove14h.cartcompose.extensions.getNeutralColor
import com.cafeinlove14h.cartcompose.extensions.getPrimaryColor
import com.cafeinlove14h.cartcompose.extensions.toColor
import com.cafeinlove14h.cartcompose.screen.cart.components.*
import vn.teko.cart.core.infrastructure.cart.data.DataState
import vn.teko.cart.domain.model.CartEntity
import javax.inject.Inject

@Composable
fun CartScreen(
    navController: NavController,
    onBack: () -> Unit = {},
    viewModel: CartViewModel = hiltNavGraphViewModel()
) {

    val context = LocalContext.current
    val cartDataState by viewModel.cartFlow.collectAsState()
    Scaffold(
        backgroundColor = context.getNeutralColor().backgroundColor.toColor(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Header(title = "Cart") {
                onBack()
            }
        },
        bottomBar = {
            CartFooter(navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (cartDataState.loading) {
                Dialog(onDismissRequest = { !cartDataState.loading }) {
                    CircularProgressIndicator(
                        color = context.getPrimaryColor().color500.toColor()
                    )
                }
            }
            cartDataState.data?.let {
                CartList(cart = it)
            }
        }
    }
}

@Composable
fun CartList(cart: CartEntity, viewModel: CartViewModel = hiltNavGraphViewModel()) {
    val sellerIds =
        cart.items.distinctBy { it.sellerId }.mapNotNull { it.product?.productInfo?.seller }

    val promotion = cart.promotions
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .background(Color.White)
    ) {
        sellerIds.forEach { seller ->
            val sellerItems = cart.items.filter { item -> seller.id == item.sellerId }
            item(key = seller.id) {
                CartSellerItemView(
                    sellerEntity = seller,
                    price = sellerItems.filter { it.isSelected }.sumOf { it.rowTotal }.toDouble(),
                    checked = sellerItems.all { it.isSelected }) {
                    viewModel.updateSeller(seller.id, it)
                }
            }
            items(items = sellerItems, key = { it.lineItemId }) {
                CartLineItemView(lineItem = it,
                    onIncrease = { quantity ->
                        viewModel.updateItem(it.lineItemId, quantity)
                    }, onDecrease = { quantity ->
                        viewModel.updateItem(it.lineItemId, quantity)
                    }, onCheckedChange = { selected ->
                        viewModel.updateItem(it.lineItemId, null, selected)
                    })
            }
        }
    }
}