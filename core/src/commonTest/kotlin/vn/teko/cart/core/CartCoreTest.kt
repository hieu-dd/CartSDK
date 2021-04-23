package vn.teko.cart.core

import io.ktor.http.*
import vn.teko.cart.core.infrastructure.cart.CartApi.Companion.ENDPOINT_IDENTITY
import vn.teko.cart.core.utils.response.sample.sample500TokenResponse
import kotlin.test.*


class CartCoreTest : BaseTest() {
    @Test
    fun testInitCart() {
        getCartManager()
        assertTrue { true }
    }

    @Test
    fun testAddNewProductWhenIdentityApiResponse500() = runTest {
        setupMock(mapOf(ENDPOINT_IDENTITY to Pair(HttpStatusCode.InternalServerError, sample500TokenResponse())))
        val result = getCartManager().addItem("190900736")
        assertEquals(result.exceptionOrNull()?.code, 500001)
    }
}