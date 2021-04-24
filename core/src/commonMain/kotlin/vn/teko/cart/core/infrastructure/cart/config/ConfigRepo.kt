package vn.teko.cart.core.infrastructure.cart.config

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import vn.teko.cart.core.db.Config
import vn.teko.cart.core.db.ConfigQueries
import vn.teko.cart.core.util.transactionWithContext

internal class ConfigRepo(
    private val configQueries: ConfigQueries,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    suspend fun getConfig(): Config? =
         configQueries.findAll().asFlow().mapToList().firstOrNull()?.firstOrNull()

    suspend fun createOrUpdate(config: Config) {
        return configQueries.transactionWithContext(backgroundDispatcher) {
            with(config) {
                configQueries.createOrUpdate(id, baseUrl, terminal, channel, tenant)
            }
        }
    }
}