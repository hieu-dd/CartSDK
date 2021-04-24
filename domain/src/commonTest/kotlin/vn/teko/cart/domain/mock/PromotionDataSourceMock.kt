package vn.teko.cart.domain.mock

import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.usecase.promotion.PromotionDataSource

open class PromotionDataSourceMock : PromotionDataSource {
    override suspend fun getOrderPromotions(skus: List<String>): List<PromotionEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getCouponDetail(
        coupon: String,
        skus: List<String>,
        userToken: String?
    ): List<PromotionEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun savePromotion(promotion: PromotionEntity, cartId: String, lineItemId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllPromotionsByCartId(cartId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCouponByCartId(cartId: String) {
        TODO("Not yet implemented")
    }
}

class PromotionDataSourceMockBuilder() {
    fun build(): PromotionDataSource {
        return object : PromotionDataSourceMock() {

        }
    }
}

fun buildPromotionDataSourceMock(block: PromotionDataSourceMockBuilder.() -> Unit): PromotionDataSource {
    return PromotionDataSourceMockBuilder().apply(block).build()
}