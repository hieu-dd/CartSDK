package vn.teko.cart.core

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.*
import vn.teko.cart.core.db.CartDB

actual fun testDbConnection(): SqlDriver {
    val app = ApplicationProvider.getApplicationContext<Application>()
    // Create AndroidSqliteDriver with name is null indicates that our database is an in-memory database
    return AndroidSqliteDriver(CartDB.Schema, app, null)
}