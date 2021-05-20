package com.cafeinlove14h.cartcompose.screen.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cafeinlove14h.cartcompose.CartSdk
import com.cafeinlove14h.cartcompose.R
import com.cafeinlove14h.cartcompose.theme.CartTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CartContainerFragment : Fragment() {

    @Inject
    lateinit var cartSdk: CartSdk

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            id = R.id.cart_fragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                CartTheme {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "cart_screen"
                    ) {
                        composable("cart_screen") {
                            CartScreen(
                                navController = navController,
                                onBack = { cartSdk.getNavigationDelegate()?.backFromCart() })
                        }
                        composable("confirmation_screen") {
                            ConfirmationScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this, true
        ) { cartSdk.getNavigationDelegate()?.backFromCart() }
    }
}