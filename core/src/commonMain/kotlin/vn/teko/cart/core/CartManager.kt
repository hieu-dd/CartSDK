package vn.teko.cart.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import org.kodein.di.bind
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.instance
import org.kodein.di.singleton
import vn.teko.cart.core.extension.preventFreeze
import vn.teko.cart.core.infrastructure.cart.config.ConfigDataSource
import vn.teko.cart.core.infrastructure.cart.data.DataState
import vn.teko.cart.core.infrastructure.cart.error.CartError
import vn.teko.cart.core.infrastructure.cart.request.CheckoutRequest
import vn.teko.cart.core.infrastructure.cart.user.User
import vn.teko.cart.core.online.OnlineCart
import vn.teko.cart.core.util.runBlockingPlatform
import vn.teko.cart.domain.model.*
import vn.teko.cart.domain.util.Result
import vn.teko.cart.domain.util.suspendableResult
import kotlin.coroutines.CoroutineContext

class CartManager internal constructor(internal val di: ConfigurableDI) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = dispatcher() + job + exceptionHandler

    private val job = SupervisorJob()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is CartError -> println("CoroutineExceptionHandler got exception with $throwable")
            else -> println("CoroutineExceptionHandler got unexpected exception ${throwable.message}")
        }
    }

    private val configDs: ConfigDataSource by di.instance()

    private val onlineCart = OnlineCart(di)

    private var getCartJob: Job? = null

    private val cartFlow =
        MutableStateFlow<DataState<CartEntity>>(DataState.Idle)

    init {
        preventFreeze()
    }

    private fun getCurrentCart() = onlineCart

    fun updateConfig(cartConfig: CartConfig) = runBlockingPlatform {
        configDs.saveConfig(cartConfig)
    }

    fun setUser(user: User) {
        di.addConfig {
            bind<User>(overrides = true) with singleton { user }
        }
    }

    suspend fun addItem(
        sku: String,
        quantity: Int? = null,
        selectPromotionId: Int? = null
    ): Result<CartItemEntity> = cartResult { getCurrentCart().addItem(sku, quantity, selectPromotionId) }


    suspend fun updateItem(
        lineItemId: String,
        quantity: Int?,
        selected: Boolean?
    ): Result<CartItemEntity> = cartResult { getCurrentCart().updateItem(lineItemId, quantity, selected) }

    suspend fun deleteItem(lineItemId: String): Result<Unit> = cartResult { getCurrentCart().deleteItem(lineItemId) }

    suspend fun updateItemsBySeller(sellerId: Int, selected: Boolean): Result<Unit> =
        cartResult { getCurrentCart().updateItemsBySeller(sellerId, selected) }

    suspend fun getAvailablePromotions(): Result<OrderCouponsEntity> =
        cartResult(refreshCart = false) { getCurrentCart().getAvailablePromotions() }

    suspend fun applyPromotion(couponCode: String): Result<PromotionEntity> =
        cartResult { getCurrentCart().applyPromotion(couponCode) }

    suspend fun deletePromotion(): Result<Unit> =
        cartResult { getCurrentCart().deletePromotion() }

    suspend fun clearCart(): Result<Unit> = cartResult { getCurrentCart().clearCart() }

    suspend fun getCart(): Result<CartEntity> = withContext(coroutineContext) {
        val oldCart = cartFlow.value
        suspendableResult {
            cartFlow.value = DataState.Loading
            getCurrentCart().getCart()
        }.also { result ->
            if (result.exceptionOrNull()?.cause is CancellationException) {
                cartFlow.value = oldCart
            } else {
                cartFlow.value =
                    if (result.isSuccess()) DataState.Success(result.get()) else DataState.Error(result.exception())
            }
        }
    }

    fun getCartFlow(refresh: Boolean = false): Flow<DataState<CartEntity>> =
        cartFlow.asStateFlow().onStart { if ((cartFlow.value is DataState.Idle) || refresh) refreshCart() }

    suspend fun addShippingAddress(shippingInfo: ShippingInfoEntity): Result<ShippingInfoEntity> =
        cartResult { getCurrentCart().addShippingAddress(shippingInfo) }

    suspend fun checkoutCart(checkoutRequest: CheckoutRequest): Result<OrderCaptureResponse.OrderCaptureData> =
        cartResult { getCurrentCart().checkoutCart(checkoutRequest) }

    fun refreshCart() {
        getCartJob?.let {
            if (it.isActive) it.cancel()
        }
        getCartJob = launch {
            getCart()
        }
    }

    private suspend fun <V : Any> cartResult(
        refreshCart: Boolean = true,
        f: suspend () -> V
    ): Result<V> = withContext(coroutineContext) {
        suspendableResult { f() }.also {
            if (refreshCart) refreshCart()
        }
    }

    fun onDestroy() {
        coroutineContext.cancel()
    }
}