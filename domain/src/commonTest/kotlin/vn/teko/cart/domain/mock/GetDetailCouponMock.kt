package vn.teko.cart.domain.mock
import vn.teko.cart.domain.usecase.promotion.GetDetailCoupon

fun buildGetDetailCouponMock(): GetDetailCoupon {
    return GetDetailCoupon(promotionDataSource = buildPromotionDataSourceMock {  })
}