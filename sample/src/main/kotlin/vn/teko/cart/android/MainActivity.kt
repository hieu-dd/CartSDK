package vn.teko.cart.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cafeinlove14h.cartcompose.screen.cart.CartContainerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import vn.teko.android.core.util.instancesmanager.AppIdentifier
import vn.teko.cart.android.navigator.AppNavigator
import vn.teko.terra.core.android.terra.TerraApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppIdentifier {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TerraApp.initializeApp(application, appIdentifier).also {
            MyApplication.initApolloInstance(
                it.getConfig("apollo").config,
                appIdentifier
            )
        }
        initNavigation()
        loadFragment(HomeFragment.newInstance())
        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_cart -> {
                    loadFragment(CartContainerFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }


    }

    private fun initNavigation() {
        AppNavigator.setActivity(this)
        AppNavigator.registerSdkNavigation()
    }

    override val appIdentifier: String
        get() = MyApplication.APP_NAME


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.myNavHostFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
