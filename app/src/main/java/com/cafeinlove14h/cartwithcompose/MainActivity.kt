package com.cafeinlove14h.cartwithcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.cafeinlove14h.cartcompose.screen.cart.CartFragment
import com.cafeinlove14h.cartwithcompose.ui.screen.productlist.ProductListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.navController.apply {
            graph = createGraph(NavGraph.NAV_ID, NavGraph.Dest.PRODUCT_LIST_FRAGMENT_ID) {
                fragment<ProductListFragment>(NavGraph.Dest.PRODUCT_LIST_FRAGMENT_ID).apply { }
                fragment<CartFragment>(NavGraph.Dest.CART_FRAGMENT_ID)
            }
        }
    }
}