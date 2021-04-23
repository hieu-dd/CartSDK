package vn.teko.cart.core.online

import org.kodein.di.DI
import org.kodein.di.instance
import org.kodein.di.instanceOrNull
import vn.teko.cart.core.CartConfig
import vn.teko.cart.core.CartInterface
import vn.teko.cart.core.infrastructure.cart.CartApi
import vn.teko.cart.core.infrastructure.cart.config.ConfigDataSource
import vn.teko.cart.core.infrastructure.cart.request.CheckoutRequest
import vn.teko.cart.core.infrastructure.cart.token.TokenDataSource
import vn.teko.cart.core.infrastructure.cart.user.User
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.ShippingInfoEntity

internal class OnlineCart(private val di: DI) : CartInterface {
    private val tokenDs: TokenDataSource by di.instance()
    private val configDS: ConfigDataSource by di.instance()

    override suspend fun generateCartToken(): String {
        val cartApi: CartApi by di.instance()
        return cartApi.generateCartToken(configDS.getConfig()).token
    }

    private suspend fun getOrCreateCartToken(): String {
        val cartApi: CartApi by di.instance()
        return tokenDs.getToken()?.value
            ?: cartApi.generateCartToken(configDS.getConfig()).token.also { tokenDs.saveToken(it) }
    }


    override suspend fun addItem(
        sku: String,
        quantity: Int?,
        selectPromotionId: Int?
    ): CartItemEntity = processToken { cartApi, token, user, cartConfig ->
        cartApi.addItem(sku, quantity, selectPromotionId, cartConfig, token, user?.userToken)
    }

    override suspend fun deleteItem(lineItemId: String) =
        processToken { cartApi, token, user, cartConfig ->
            cartApi.deleteItem(lineItemId, cartConfig, token, user?.userToken)
        }

    override suspend fun updateItem(lineItemId: String, quantity: Int?, selected: Boolean?) =
        processToken { cartApi, token, user, cartConfig ->
            cartApi.updateItem(lineItemId, quantity, selected, cartConfig, token, user?.userToken)
        }

    override suspend fun updateItemsBySeller(sellerId: Int, selected: Boolean) =
        processToken { cartApi, token, user, cartConfig ->
            cartApi.updateItemBySeller(sellerId, selected, cartConfig, token, user?.userToken)
        }


    override suspend fun getAvailablePromotions() =
        processToken { cartApi, token, user, cartConfig ->
            cartApi.getAvailablePromotions(cartConfig, token, user?.userToken)
        }

    override suspend fun applyPromotion(couponCode: String) =
        processToken { cartApi, token, user, cartConfig ->
            cartApi.applyPromotion(couponCode, cartConfig, token, user?.userToken)
        }

    override suspend fun deletePromotion() = processToken { cartApi, token, user, cartConfig ->
        cartApi.deletePromotion(cartConfig, token, user?.userToken)
    }


    override suspend fun getCart() = processToken { cartApi, token, user, cartConfig ->
        cartApi.getCart(cartConfig, token, user?.userToken)
    }

    override suspend fun clearCart() = processToken { cartApi, token, user, cartConfig ->
        cartApi.clearCart(cartConfig, token, user?.userToken)
    }


    override suspend fun addShippingAddress(shippingInfo: ShippingInfoEntity) =
        processToken { cartApi, token, user, cartConfig ->
            cartApi.addShippingAddress(cartConfig, shippingInfo, token, user?.userToken)
        }


    override suspend fun checkoutCart(checkoutRequest: CheckoutRequest) =
        processToken { cartApi, token, user, cartConfig ->
            cartApi.checkoutCart(checkoutRequest, cartConfig, token, user?.userToken)
        }

    private suspend fun <T> processToken(block: suspend (cartApi: CartApi, token: String, user: User?, CartConfig) -> T): T {
        val cartApi: CartApi by di.instance()
        val token = getOrCreateCartToken()
        val cartConfig = configDS.getConfig()
        val user: User? by di.instanceOrNull()
        return block(cartApi, token, user, cartConfig)
    }

}