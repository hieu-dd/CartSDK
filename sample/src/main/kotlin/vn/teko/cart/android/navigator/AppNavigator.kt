package vn.teko.cart.android.navigator

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cafeinlove14h.cartcompose.CartNavigationDelegate
import com.cafeinlove14h.cartcompose.CartSdk
import com.cafeinlove14h.cartcompose.extensions.getAppName
import vn.teko.cart.android.AppTestBus
import vn.teko.cart.android.LocalData
import vn.teko.cart.android.R
import vn.teko.cart.android.bus.model.request.PaymentRequest
import vn.teko.terra.core.android.terra.TerraApp

@SuppressLint("StaticFieldLeak")
internal object AppNavigator {
    fun registerSdkNavigation() {
        val terraApp = TerraApp.getInstance(activity!!.getAppName())
        AppTestBus.getInstance(activity!!, terraApp)
        CartSdk.getInstance(
            activity!!,
            terraApp,
            LocalData.terminal,
            LocalData.channel,
            LocalData.channelType,
            LocalData.channelId
        )
            .setNavigationDelegate(object : CartNavigationDelegate {
                override fun backFromCart() {
                    popBackStack()
                }

                override fun goToBuy() {}

                override fun goToLogin() {
                    getMainNavigation()?.navigate(R.id.action_cartContainerFragment_to_loginFragment)
                }

                override fun goToAddress() {}

                override fun goToCustomer(customerId: String?) {
                    getMainNavigation()?.navigate(R.id.action_cartContainerFragment_to_customerFragment)
                }

                override fun goToShippingAddress(customerId: String) {
                    getMainNavigation()?.navigate(R.id.action_cartContainerFragment_to_customerFragment)
                }

                override fun goToPayment(paymentRequest: PaymentRequest) {
                    Toast.makeText(activity, "paymentRequest: $paymentRequest", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private var activity: Activity? = null

    internal fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    fun popBackStack() {
        getMainNavigation()?.popBackStack()
    }

    fun getMainNavigation(): NavController? =
        activity?.let { Navigation.findNavController(it, R.id.homeNavHostFragment) }
}