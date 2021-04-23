package com.cafeinlove14h.cartwithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import com.cafeinlove14h.cartwithcompose.ui.screen.ProductListScreen
import com.cafeinlove14h.cartwithcompose.ui.theme.CartWithComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var client: HttpClient

    private val mainViewModel: MainViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartWithComposeTheme {
                ProductListScreen(mainViewModel)
            }
        }
    }
}