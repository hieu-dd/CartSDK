package vn.teko.cart.core.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import vn.teko.cart.core.db.CartDB


actual val PlatformModule: DI.Module = DI.Module(name = "Platform") {
    bind<SqlDriver>() with singleton {
        AndroidSqliteDriver(CartDB.Schema, instance(), "CartDB")
    }

    bind<HttpClientEngine>() with singleton {
        OkHttp.create {}
    }
}