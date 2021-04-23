package vn.teko.cart.core.di

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import org.kodein.di.*
import vn.teko.cart.core.db.CartDB

actual val PlatformModule: DI.Module = DI.Module(name = "Platform") {

    bind<SqlDriver>() with singleton {
        NativeSqliteDriver(CartDB.Schema, "CartDB")
    }

    bind<HttpClientEngine>() with singleton {
        Ios.create {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }
}