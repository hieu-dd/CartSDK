package vn.teko.cart.domain.mock

import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.model.ShippingInfoEntity
import vn.teko.cart.domain.usecase.cart.CartDataSource

open class CartDataSourceMock: CartDataSource {
    override suspend fun saveCart(cartEntity: CartEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getCart(cartId: String?, userId: String?, createIfNotExists: Boolean): CartEntity {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCart(cartId: String?, userId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSelectedItems(cartId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUnselectedItems(cartId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveCouponPromotion(cartId: String, couponPromotion: PromotionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCouponPromotion(cartId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addShippingInfo(cartId: String, shippingInfo: ShippingInfoEntity): ShippingInfoEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getShippingInfo(cartId: String): ShippingInfoEntity? {
        TODO("Not yet implemented")
    }

}

class CartDataSourceBuilder() {
    var saveCart: (suspend (cart: CartEntity) -> Unit)? = null
//    var getCartByIdOrUserId: (suspend  (cartId: String?, userId: String) -> CartEntity)? = null

    fun build(): CartDataSource {
        return object : CartDataSourceMock() {
            override suspend fun saveCart(cartEntity: CartEntity) {
                saveCart?.invoke(cartEntity) ?: super.saveCart(cartEntity)
            }

//            override suspend fun getCartByIdOrUserId(cartId: String?, userId: String): CartEntity {
//                return getCartByIdOrUserId?.invoke(cartId, userId) ?: super.getCartByIdOrUserId(cartId, userId)
//            }
        }
    }
}

fun buildCartDataSourceMock(block: CartDataSourceBuilder.() -> Unit): CartDataSource {
    return CartDataSourceBuilder().apply(block).build()
}
