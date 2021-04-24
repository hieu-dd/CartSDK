package vn.teko.cart.core.infrastructure.cart.token

import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import vn.teko.cart.core.db.Token
import vn.teko.cart.core.db.TokenQueries
import vn.teko.cart.core.util.transactionWithContext

internal class TokenRepo(
    private val tokenQueries: TokenQueries,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    suspend fun getToken(): Token? {
        return tokenQueries
            .findAll()
            .asFlow()
            .first()
            .executeAsOneOrNull()
    }

    suspend fun clearToken() {
        return tokenQueries.transactionWithContext(backgroundDispatcher) {
            tokenQueries.deleteAll()
        }
    }

    suspend fun saveToken(token: String) {
        return tokenQueries.transactionWithContext(backgroundDispatcher) {
            tokenQueries.insertOrReplaceIfExists(token)
        }
    }
}