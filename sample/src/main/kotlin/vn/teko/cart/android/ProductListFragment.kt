package vn.teko.cart.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter.Companion.items
import vn.teko.android.core.util.android.extension.showToast
import vn.teko.apollo.component.navigation.header.ApolloSearchHeader
import vn.teko.cart.android.busmodels.AddProductToCartRequest
import vn.teko.cart.android.busmodels.CartItemEntity
import vn.teko.cart.android.databinding.FragmentProductListBinding
import vn.teko.cart.android.event.EventNames.CART_ADD_PRODUCT
import com.cafeinlove14h.cartcompose.extensions.getAppName
import dagger.hilt.android.AndroidEntryPoint
import vn.teko.cart.android.uimodels.SampleProductItemView
import vn.teko.terra.core.android.terra.TerraApp

@AndroidEntryPoint
class ProductListFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductListBinding
    private lateinit var itemAdapter: ItemAdapter<GenericItem>
    private lateinit var fastAdapter: FastAdapter<GenericItem>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProductListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val terraApp = TerraApp.initializeApp(
            requireActivity().application,
            requireActivity().getAppName()
        )
        itemAdapter = items()
        viewBinding.apply {
            fastAdapter = FastAdapter.with(itemAdapter)
            rvProducts.adapter = fastAdapter
            header.setLeftClickListener {
                findNavController().navigate(R.id.action_productListFragment_to_cartContainerFragment)
            }
            header.setListener(object : ApolloSearchHeader.SearchViewListener {
                override fun onClick() {}

                override fun onSearch(text: String) {}

                override fun onSubmit(text: String) {
                    setData(terraApp, "\"$text\"")
                }
            })
        }

        setData(terraApp)
        requireActivity().onBackPressedDispatcher.addCallback(
            this, true
        ) { requireActivity().finish() }

    }

    fun setData(terraApp: TerraApp, skus: String = "") {
        try {
            lifecycleScope.launchWhenStarted {
                val products = AppTestBus.getProducts(skus)
                itemAdapter.clear()
                itemAdapter.add(products.map { product ->
                    SampleProductItemView(product).apply {
                        onAddProduct = View.OnClickListener {
                            lifecycleScope.launchWhenStarted {
                                val result = terraApp.getTerraBus()
                                    .request(
                                        CART_ADD_PRODUCT,
                                        AddProductToCartRequest(product.productInfo.sku),
                                        CartItemEntity::class.java
                                    )
                                Toast.makeText(
                                    requireContext(),
                                    if (result.isSuccess()) "Them san pham thanh cong" else "Them san pham that bai",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            showToast(e.message.orEmpty(), Toast.LENGTH_SHORT)
            setData(terraApp)
        }
    }
}