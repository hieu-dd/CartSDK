package com.cafeinlove14h.cartcompose.screen.cart.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun CartFooter(navController: NavController) {
    Row() {
        Button(onClick = { navController.navigate("confirmation_screen") }) {
            Text(text = "Continue")
        }
    }
}