package vn.teko.cart.domain.usecase.identity

import vn.teko.cart.domain.exception.CartIdentityError
import vn.teko.cart.domain.mock.buildCartDataSourceMock
import vn.teko.cart.domain.model.CartTokenPayload
import vn.teko.cart.domain.model.UserEntity
import vn.teko.cart.domain.util.Result
import vn.teko.cart.domain.util.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GenerateCartTokenTest {

    @Test
    fun test_generateCartToken_success() = runTest {
        val mockCartToken = ""
        val mockCartTokenService = object : IdentityService {
            override suspend fun encodeCartToken(payload: CartTokenPayload): String {
                return mockCartToken
            }

            override suspend fun identifyUser(userToken: String): UserEntity {
                TODO("Not yet implemented")
            }

            override suspend fun decodeCartToken(cartToken: String): CartTokenPayload {
                TODO("Not yet implemented")
            }
        }
        val mockCartDataSource = buildCartDataSourceMock {  }

        val generateCartToken = GenerateCartToken(mockCartTokenService, mockCartDataSource)
        val result = generateCartToken(
            GenerateCartToken.GenerateCartTokenParams(
                tenant = "vnshop",
                cartId = "a4d685c4-b87c-4004-879e-655a0b4d9e2b",
                userId = null
            )
        )

        assertTrue(result is Result.Success)
        assertEquals(mockCartToken, result.value.token)
    }

    @Test
    fun test_generateCartToken_failure_invalidParams() = runTest {
        val mockCartToken = ""
        val mockCartTokenService = object : IdentityService {
            override suspend fun encodeCartToken(payload: CartTokenPayload): String {
                return mockCartToken
            }

            override suspend fun identifyUser(userToken: String): UserEntity {
                TODO("Not yet implemented")
            }

            override suspend fun decodeCartToken(cartToken: String): CartTokenPayload {
                TODO("Not yet implemented")
            }
        }

        val mockCartDataSource = buildCartDataSourceMock {  }

        val generateCartToken = GenerateCartToken(mockCartTokenService, mockCartDataSource)
        val result = generateCartToken(
            GenerateCartToken.GenerateCartTokenParams(
                tenant = "vnshop",
                cartId = "invalid-cart-id",
                userId = null
            )
        )

        assertTrue(result is Result.Failure)
        assertEquals(CartIdentityError.GenerateCartToken.InvalidParams, result.error)
    }
}