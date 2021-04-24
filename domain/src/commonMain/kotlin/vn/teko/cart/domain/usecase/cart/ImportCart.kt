package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.extension.buildCartData
import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.ProductEntity
import vn.teko.cart.domain.usecase.UseCase

open class ImportCart(
    private val productDataSource: ProductDataSource
) : UseCase<ImportCart.ImportCartParams, CartEntity>() {

    override suspend fun run(params: ImportCartParams) = coroutineScope {

        params.cart.let { cart ->
            /**
             * Step1: Fetch new product info to build full cart data.
             * If product is not available then throw an NonExistentPromotion exception
             */
            val productSkus = cart.items.map { it.sku }.distinct()
            val products = productDataSource.getValidProducts(productSkus)
            if (params.isCheckout && !products.map { it.productInfo.sku }.containsAll(productSkus)) {
                throw CartError.CheckoutError.InvalidProduct
            }
            /**
             * Import cart from products data
             */
            CartEntity.importFrom(cart.id, cart.buildCartData(products))
                .apply {
                    isNew = cart.isNew
                    userId = cart.userId
                }
        }
    }

    class ImportCartParams(
        val cart: CartEntity,
        val isCheckout: Boolean = false
    ) : UseCase.Params()
}