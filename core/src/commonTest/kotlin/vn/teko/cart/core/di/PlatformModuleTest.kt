package vn.teko.cart.core.di

import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import vn.teko.cart.core.testDbConnection
import io.ktor.client.engine.mock.*

val PlatformModuleTest: DI.Module = DI.Module(name = "Platform") {
    bind<SqlDriver>() with singleton {
        testDbConnection()
    }

    bind<HttpClientEngine>() with singleton{
        MockEngine.create {}
    }
}