package com.cafeinlove14h.cartwithcompose

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import vn.teko.cart.core.CartManager
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val client: HttpClient, private val cartManager: CartManager) :
    ViewModel() {
}