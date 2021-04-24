package vn.teko.cart.core.infrastructure.cart.token

import vn.teko.cart.core.db.Token

internal interface TokenDataSource {
    suspend fun getToken(): Token?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}