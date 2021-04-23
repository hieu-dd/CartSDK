package vn.teko.cart.domain.usecase.promotion

import vn.teko.cart.domain.exception.Error
import vn.teko.cart.domain.exception.ValidationDataError
import vn.teko.cart.domain.extension.getSelectedItems
import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.usecase.UseCase

class GetDetailCoupon(
    private val promotionDataSource: PromotionDataSource
) : UseCase<GetDetailCoupon.GetCouponDetailParams, PromotionEntity>() {

    override suspend fun run(params: GetCouponDetailParams): PromotionEntity {
        /**
         * Fetch PPM for Coupon promotions
         */
        return promotionDataSource.getCouponDetail(
            params.coupon!!,
            params.cart.getSelectedItems().map { it.sku },
            params.userToken
        )
    }

    /**
     * @property cart : cart with full data
     * @property userToken : token of user
     * @property coupon : coupon that want to fetch detail
     */
    data class GetCouponDetailParams(val cart: CartEntity, val userToken: String?, val coupon: String?) :
        UseCase.Params() {
        override fun selfValidate(): Error? = when {
            coupon.isNullOrBlank() -> ValidationDataError.FieldIsInvalid("coupon")
            else -> null
        }
    }

}