package vn.teko.cart.core.extension

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vn.teko.cart.core.CartManager
import vn.teko.cart.core.infrastructure.cart.data.DataState
import vn.teko.cart.domain.exception.Error
import vn.teko.cart.domain.model.CartEntity
import kotlin.native.concurrent.ensureNeverFrozen

fun CartManager.observe(
    onLoading: () -> Unit,
    onSuccess: (CartEntity) -> Unit,
    onError: (Error) -> Unit
) = getCartFlow().onEach { dataState ->
    when (dataState) {
        is DataState.Success -> {
            onSuccess(dataState.data)
        }
        is DataState.Error -> {
            onError(dataState.throwable)
        }
        DataState.Loading -> {
            onLoading()
        }
        else -> {
        }
    }
}.launchIn(this)

internal actual fun CartManager.preventFreeze() {
    this.ensureNeverFrozen()
}