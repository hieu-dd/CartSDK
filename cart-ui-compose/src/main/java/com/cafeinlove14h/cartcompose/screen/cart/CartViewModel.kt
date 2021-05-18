package com.cafeinlove14h.cartcompose.screen.cart

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafeinlove14h.cartcompose.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import vn.teko.cart.android.bus.CartBus
import vn.teko.cart.core.infrastructure.cart.data.DataState
import vn.teko.cart.domain.model.CartEntity
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CartViewModel
@Inject
internal constructor(private val cartBus: CartBus) : ViewModel() {
    val cartFlow: MutableStateFlow<UiState<CartEntity>> =
        MutableStateFlow(UiState(false, null, null))

    init {
        cartBus.getCartFlow().onEach {
            when (it) {
                is DataState.Loading -> cartFlow.value = cartFlow.value.copy(loading = true)
                is DataState.Error -> cartFlow.value =
                    cartFlow.value.copy(loading = false, exception = Exception())
                is DataState.Success -> cartFlow.value =
                    cartFlow.value.copy(loading = true, exception = null, data = it.data)
            }
        }.launchIn(viewModelScope)
    }

    fun updateItem(lineItemId: String, quantity: Int) {
        viewModelScope.launch {
            cartBus.updateItem(lineItemId, quantity, true)
        }
    }
}