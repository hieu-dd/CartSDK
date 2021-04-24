package vn.teko.cart.domain.model

data class CartTokenPayload(
    /**
     * The "iss" (issuer) claim identifies the principal that issued the
     * JWT. When there are many tenants may use this service, we need to
     * add field to control.
     */
    val iss: String?,

    /**
     * Cart id
     */
    val cid: String,
)