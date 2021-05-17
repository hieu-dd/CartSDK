package vn.teko.cart.android.uimodels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import vn.teko.cart.android.R
import vn.teko.cart.android.busmodels.DiscoveryProduct
import vn.teko.cart.android.databinding.CartSampleProductItemBinding
import com.cafeinlove14h.cartcompose.utils.CurrencyUtils.formatMoney

class SampleProductItemView(val product: DiscoveryProduct) :
    AbstractBindingItem<CartSampleProductItemBinding>() {

    var onAddProduct: View.OnClickListener? = null

    override val type: Int
        get() = R.id.cart_sample_product_item_id

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CartSampleProductItemBinding {
        return CartSampleProductItemBinding.inflate(inflater, parent, false)
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    override fun bindView(binding: CartSampleProductItemBinding, payloads: List<Any>) {
        binding.apply {
            name.text = product.productInfo.name
            price.text = formatMoney(product.prices.firstOrNull()?.sellPrice?.toDouble() ?: 0.0)
            seller.text = "Seller: ${product.productInfo.seller.displayName}"
            sku.text = "Sku : ${product.productInfo.sku}"
            image.load(product.productInfo.imageUrl)
            add.setOnClickListener {
                onAddProduct?.onClick(it)
            }
        }
    }
}