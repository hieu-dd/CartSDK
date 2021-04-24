package vn.teko.cart.core.di

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vn.teko.cart.core.backgroundDispatcher
import vn.teko.cart.core.db.CartDB
import vn.teko.cart.core.infrastructure.cart.CartApi
import vn.teko.cart.core.infrastructure.cart.config.ConfigDS
import vn.teko.cart.core.infrastructure.cart.config.ConfigDataSource
import vn.teko.cart.core.infrastructure.cart.config.ConfigRepo
import vn.teko.cart.core.infrastructure.cart.token.TokenDS
import vn.teko.cart.core.infrastructure.cart.token.TokenDataSource
import vn.teko.cart.core.infrastructure.cart.token.TokenRepo

val DataModule: DI.Module = DI.Module(name = "Data") {
    bind<CartDB>() with singleton { CartDB(instance()) }

    bind<CartApi>() with singleton { CartApi(instance()) }

    bind<TokenRepo>() with singleton { TokenRepo(instance<CartDB>().tokenQueries, backgroundDispatcher()) }

    bind<TokenDataSource>() with singleton { TokenDS(instance()) }

    bind<ConfigRepo>() with singleton { ConfigRepo(instance<CartDB>().configQueries, backgroundDispatcher()) }

    bind<ConfigDataSource>() with singleton { ConfigDS(instance()) }

    bind<HttpClient>() with singleton {
        HttpClient(instance()) {
            expectSuccess = false
            Json {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    isLenient = false
                    ignoreUnknownKeys = true
                    allowSpecialFloatingPointValues = true
                    useArrayPolymorphism = false
                })
            }
            Logging {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }
}