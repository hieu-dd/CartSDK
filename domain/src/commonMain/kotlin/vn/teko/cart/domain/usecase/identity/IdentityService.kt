package vn.teko.cart.domain.usecase.identity

import vn.teko.cart.domain.model.CartTokenPayload
import vn.teko.cart.domain.model.UserEntity

interface IdentityService {

    suspend fun decodeCartToken(cartToken: String): CartTokenPayload

    suspend fun encodeCartToken(payload: CartTokenPayload): String

    suspend fun identifyUser(userToken: String): UserEntity

}