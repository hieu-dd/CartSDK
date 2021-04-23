package vn.teko.cart.core.infrastructure.cart.data

import vn.teko.cart.domain.exception.Error as DomainError

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val throwable: DomainError) : DataState<Nothing>()
    object Idle : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}

inline fun <T, R> DataState<T>.map(transform: (T) -> R): DataState<R> {
    return when (this) {
        is DataState.Idle -> this
        is DataState.Loading -> this
        is DataState.Error -> this
        is DataState.Success<*> -> DataState.Success(transform(data as T))
    }
}
