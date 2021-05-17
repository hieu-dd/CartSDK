package vn.teko.cart.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.runBlocking
import vn.teko.android.core.util.android.extension.hideKeyboard
import vn.teko.apollo.extension.getApolloTheme
import vn.teko.cart.android.databinding.FragmentLoginBinding
import com.cafeinlove14h.cartcompose.extensions.getAppName
import dagger.hilt.android.AndroidEntryPoint
import vn.teko.terra.core.android.terra.TerraApp

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var viewBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val terraApp = TerraApp.Companion.initializeApp(
            requireActivity().application,
            requireActivity().getAppName()
        )

        viewBinding.apply {
            root.setBackgroundColor(root.context.getApolloTheme().getNeutralColor().backgroundColor)

            btnLogin.setOnClickListener {
                runBlocking {
                    hideKeyboard()
                    val authToken = edtToken.text.toString()
                    AppTestBus.getInstance(requireActivity(), terraApp).setAccessToken(authToken)
                    findNavController().navigateUp()
                    Toast.makeText(
                        root.context,
                        "Dang nhap thanh cong",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this, true
        ) { findNavController().navigateUp() }
    }
}