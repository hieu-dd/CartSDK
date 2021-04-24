package vn.teko.cart.domain.extension

import vn.teko.cart.domain.usecase.Validator

fun List<Validator>.validate() {
    forEach { validator ->
        val error = validator.validate()
        if (error != null) throw error
    }
}

