package vn.teko.cart.domain.mock

import vn.teko.cart.domain.usecase.cart.RemoveLineItem

class RemoveLineItemParamsMockBuilder {
    var cartId: String? = "a4d685c4-b87c-4004-879e-655a0b4d9e2b"
    var lineItemId: String = "a4d685c4-b87c-4004-879e-655a0b4d9e2b"
    var userId: String? = "d4a6c063635f4568b94dee1b1441a0e3"
    var userToken: String? = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImQ0NjhhMWMyYWYzNTRjOGM4NDc0OTgwYjM5ZDdhNTZhIiwidHlwIjoiSldUIn0.eyJqdGkiOiIwMDgwOTJjYjFlMzgzZmNmZWRmNzhiZDE3NjdjOGMzZGJjMGExZDQwMzhjMTIwZGM1NWUzZTk3MTVjZGZhNDc4IiwiaXNzIjoiaHR0cHM6Ly9vYXV0aC5zdGFnZS50ZWtvYXBpcy5uZXQiLCJzdWIiOiJkNGE2YzA2MzYzNWY0NTY4Yjk0ZGVlMWIxNDQxYTBlMyIsImF1ZCI6ImZkNzVhNTQ1MmFjNjRhYTZiMTA4YTU0YjUzNDYxZTk5IiwiaWF0IjoxNjExNTUwMjg4LCJleHAiOjE2MTE1NTM4ODgsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgdXMgb20gcHBtIn0.nr3P1VtPKAfTy3GklReQU9psBybiWASp6KRbTPTEn0EVJSlsbZidkCHXJM1xsArcpZRQwtySV42Pi41sfIPPsbxANBuREgjFwiaJAaz5GlcR_xHjcwMIeznSRgL18-VfoRUtni50HdZjabsCaOh0TBtl_iLjhweRvAGNKoVohumCrHCw__hgy_LY7SN1fvP7Zu4CfdW6nRVoUIQox_irFxDt_4AeSq7pCEUbNNmo2kvhAgYS4hxGAyZCdPZKlB3N8EfgTCcb6IjNfIEKKweANq9_x4CU9xX9arCUNx8vi-Ty6x4pQ1bBPSlHkPP95HFGlAAsyE3yB9gcbY3F870pCWPda6DTw60dh_raRB3ecnRnbRZLb8ON_G7z0_zKFzReZUdR6QzcHjvstl_Kn3z6kev0dbjz0PJ164H77_zRw6mLfonttAnXG7yR7Q7oRnemwlUF0jqw_dkHGmpgZrcbtSXLx8cdAlUB0jhttwCpCfseGB1tZrjdt11WrN1_jUE31omZppxV_lE2Xc7GcHZCHwp-BAnXkv8EgcUWXh6UvUQHUI8YTi0KiVuYqXOedshV2JmIy2iGnOol2Awgt2rxUDkLoRSeVFq0kV-eDE1NG52aqEFjocMuE73-3HJc_5SRkPvaxYpicL8nNEHhbvraVJgx3yfOnUC1KN9cnHppD2Q"

    fun cartId(cartId: String) = apply { this.cartId = cartId }
    fun lineItemId(lineItemId: String) = apply { this.lineItemId = lineItemId }
    fun userId(userId: String) = apply { this.userId = userId }
    fun userToken(userToken: String) = apply { this.userToken = cartId }

    fun build() = RemoveLineItem.RemoveLineItemParams(this.cartId, this.lineItemId, this.userId, this.userToken)
}
