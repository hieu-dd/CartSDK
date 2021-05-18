package com.cafeinlove14h.cartcompose.screen.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
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
            // In order for savedState to work, the same ID needs to be used for all instances.
            id = R.id.cart_fragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                CartTheme {
                    CartScreen()
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