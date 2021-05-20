package com.cafeinlove14h.cartcompose.screen.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.cafeinlove14h.cartcompose.extensions.getNeutralColor
import com.cafeinlove14h.cartcompose.extensions.toColor
import com.cafeinlove14h.cartcompose.screen.cart.components.Header

@Composable
fun ConfirmationScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        backgroundColor = context.getNeutralColor().backgroundColor.toColor(),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Header(title = "Confirmation") {
                navController.navigateUp()
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

        }
    }
}