package vn.teko.cart.domain.usecase.cart

import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.mock.RemoveLineItemParamsMockBuilder
import vn.teko.cart.domain.mock.buildCartDataSourceMock
import vn.teko.cart.domain.mock.buildGetDetailCouponMock
import vn.teko.cart.domain.mock.buildImportCartMock
import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.util.Result
import vn.teko.cart.domain.util.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemoveLineItemTest {
    @Test
    @Ignore
    fun test_removeLineItem_success() = runTest {
        val mockCartDataSource = buildCartDataSourceMock {}
        val mockImportCart = buildImportCartMock { }
        val removeLineItem = RemoveCartItem(
            cartDataSource = mockCartDataSource,
            importCart = mockImportCart,
            getDetailCoupon = buildGetDetailCouponMock()
        )
        val validCartIdResult = removeLineItem(
            RemoveLineItemParamsMockBuilder().build()
        )

        assertTrue(validCartIdResult is Result.Success)

    }

    @Test
    fun test_removeLineItem_failure_invalidParams() = runTest {
        val mockCartDataSource = buildCartDataSourceMock { }
        val mockImportCart = buildImportCartMock { }
        val removeLineItem = RemoveCartItem(
            cartDataSource = mockCartDataSource,
            importCart = mockImportCart,
            getDetailCoupon = buildGetDetailCouponMock()
        )


        val invalidCartIdResult = removeLineItem(
            RemoveLineItemParamsMockBuilder().cartId("not an uuid string").build()
        )

        assertTrue(invalidCartIdResult is Result.Failure)
        assertEquals(CartError.InvalidCartId, invalidCartIdResult.error)

        val invalidLineItemIdResult = removeLineItem(
            RemoveLineItemParamsMockBuilder().lineItemId("not an uuid string").build()
        )

        assertTrue(invalidLineItemIdResult is Result.Failure)

    }
}