package vn.teko.cart.core

import com.squareup.sqldelight.db.SqlDriver
import vn.teko.cart.core.db.CartDB
import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import io.ktor.client.*

actual fun testDbConnection(): SqlDriver {
    val schema = CartDB.Schema
    return NativeSqliteDriver(
        DatabaseConfiguration(
            name = "cartDB",
            version = schema.version,
            create = { connection ->
                wrapConnection(connection) { schema.create(it) }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) { schema.migrate(it, oldVersion, newVersion) }
            },
            inMemory = true
        )
    )
}