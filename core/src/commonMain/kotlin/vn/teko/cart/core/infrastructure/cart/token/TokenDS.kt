package vn.teko.cart.core.infrastructure.cart.token

internal class TokenDS(private val tokenRepo: TokenRepo): TokenDataSource {
    override suspend fun getToken() = tokenRepo.getToken()

    override suspend fun saveToken(token: String) = tokenRepo.saveToken(token)

    override suspend fun clearToken() {
        tokenRepo.clearToken()
    }
}