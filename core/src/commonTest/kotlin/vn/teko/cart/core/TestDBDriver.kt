package vn.teko.cart.core

import com.squareup.sqldelight.db.SqlDriver

internal expect fun testDbConnection(): SqlDriver
