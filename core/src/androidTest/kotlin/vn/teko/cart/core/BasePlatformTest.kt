package vn.teko.cart.core

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import vn.teko.cart.core.extension.createTestCart
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


@RunWith(AndroidJUnit4::class)
actual abstract class BasePlatformTest {
    private lateinit var cartManager: CartManager

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeTest
    fun setupTest() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDownTest() {
        Dispatchers.resetMain()
    }

    actual fun <T> runTest(block: suspend CoroutineScope.() -> T) {
        runBlocking { block() }
    }

    actual fun initCartManager(cartConfig: CartConfig) {
        val context = ApplicationProvider.getApplicationContext<Application>()
        cartManager = CartFactory.createTestCart(cartConfig, context)
    }

    actual fun getCartManager() = cartManager
}