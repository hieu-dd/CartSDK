package vn.teko.cart.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.runBlocking
import vn.teko.apollo.extension.getApolloTheme
import vn.teko.cart.android.busmodels.CustomerProfile
import vn.teko.cart.android.busmodels.customer
import vn.teko.cart.android.databinding.FragmentCustomerBinding
import vn.teko.cart.android.event.EventNames.USER_PROFILE_SET_SELECT_CUSTOMER
import com.cafeinlove14h.cartcompose.extensions.getAppName
import vn.teko.terra.core.android.terra.TerraApp

class CustomerFragment : Fragment() {
    lateinit var viewBinding: FragmentCustomerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCustomerBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val terraApp = TerraApp.initializeApp(
            requireActivity().application,
            requireActivity().getAppName()
        )
        viewBinding.apply {
            root.setBackgroundColor(root.context.getApolloTheme().getNeutralColor().whiteColor)

            btnOk.setOnClickListener {
                runBlocking {
                    val result = terraApp.getTerraBus().request(
                        USER_PROFILE_SET_SELECT_CUSTOMER,
                        customer(
                            name.text.toString(),
                            company.isChecked,
                            retail.isChecked,
                            shipping.isChecked
                        ),
                        CustomerProfile::class.java
                    )
                    if (result.isSuccess()) {
                        findNavController().navigateUp()
                    }
                }
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this, true
        ) { findNavController().navigateUp() }
    }
}