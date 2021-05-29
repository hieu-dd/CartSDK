package com.cafeinlove14h.cartcompose.screen.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cafeinlove14h.cartcompose.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance
import vn.teko.cart.android.bus.CartBus
import vn.teko.cart.core.infrastructure.cart.data.DataState
import vn.teko.cart.domain.model.CartEntity

class CartViewModel(app: Application) : AndroidViewModel(app), DIAware {
    override val di by closestDI()

    val cartFlow: MutableStateFlow<UiState<CartEntity>> =
        MutableStateFlow(UiState(false, null, null))

    val cartBus: CartBus by instance()

    init {
        cartBus.getCartFlow().onEach {
            when (it) {
                is DataState.Loading -> cartFlow.value = cartFlow.value.copy(loading = true)
                is DataState.Error -> cartFlow.value =
                    cartFlow.value.copy(loading = false, exception = Exception())
                is DataState.Success -> cartFlow.value =
                    cartFlow.value.copy(loading = false, exception = null, data = it.data)
            }
        }.launchIn(viewModelScope)
    }

    fun updateItem(lineItemId: String, quantity: Int?, selected: Boolean? = null) {
        viewModelScope.launch {
            cartBus.updateItem(lineItemId, quantity, selected)
        }
    }

    fun updateSeller(sellerId: Int, selected: Boolean) {
        viewModelScope.launch {
            cartBus.updateItemsBySeller(sellerId, selected)
        }
    }
}