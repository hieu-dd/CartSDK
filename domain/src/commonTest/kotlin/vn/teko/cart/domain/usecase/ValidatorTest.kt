package vn.teko.cart.domain.usecase

import vn.teko.cart.domain.exception.ValidationDataError
import kotlin.test.Test
import kotlin.test.assertTrue


class ValidatorTest {
    @Test
    fun intValidationTest() {
        val quantity = 3
        assertTrue(
            NumberValidator(
                quantity, "quantity", null, null
            ).validate() == null
        )
        assertTrue(
            NumberValidator(
                quantity, "quantity", 1, null
            ).validate() == null
        )
        assertTrue(
            (NumberValidator(
                quantity, "quantity", 6, null
            ).validate() as ValidationDataError.FieldNumberOutOfRange).minimum.toInt() == 6
        )
        assertTrue(
            (NumberValidator(
                quantity, "quantity", 1, 2
            ).validate() as ValidationDataError.FieldNumberOutOfRange).minimum.toInt() == 1
        )
        assertTrue(
            (NumberValidator(
                quantity, "quantity", 6, null
            ).validate() as ValidationDataError.FieldNumberOutOfRange).maximum == "null"
        )
        assertTrue(
            NumberValidator(
                quantity, "quantity", null, 4
            ).validate() == null
        )
    }

    @Test
    fun doubleValidationTest() {
        val quantity = 3.0
        assertTrue(
            NumberValidator(
                quantity, "quantity", null, null
            ).validate() == null
        )
        assertTrue(
            NumberValidator(
                quantity, "quantity", 1.0, null
            ).validate() == null
        )
        assertTrue(
            (NumberValidator(
                quantity, "quantity", 6.0, null
            ).validate() as ValidationDataError.FieldNumberOutOfRange).minimum == "6.0"
        )
        assertTrue(
            (NumberValidator(
                quantity, "quantity", 1.0, 2.0
            ).validate() as ValidationDataError.FieldNumberOutOfRange).minimum == "1.0"
        )
        assertTrue(
            (NumberValidator(
                quantity, "quantity", 6.0, null
            ).validate() as ValidationDataError.FieldNumberOutOfRange).maximum == "null"
        )
        assertTrue(
            NumberValidator(
                quantity, "quantity", null, 4.0
            ).validate() == null
        )
    }
}