package vn.teko.cart.domain.usecase.promotion

import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.exception.PromotionError
import vn.teko.cart.domain.extension.checkValidPromotion
import vn.teko.cart.domain.extension.getSelectedItems
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.StringValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.cart.CartDataSource
import vn.teko.cart.domain.usecase.cart.ImportCart
import vn.teko.cart.domain.util.getOrElse

class ApplyCoupon(
    private val cartDataSource: CartDataSource,
    private val promotionDataSource: PromotionDataSource,
    private val importCart: ImportCart,
) : UseCase<ApplyCoupon.ApplyCouponParams, PromotionEntity>() {

    override suspend fun run(params: ApplyCouponParams): PromotionEntity = coroutineScope {
        val cart = cartDataSource.getCart(params.cartId, params.userId)
        val fullCart = importCart(ImportCart.ImportCartParams(cart)).getOrElse { throw it }
        val selectedItems = fullCart.getSelectedItems()
        val promotion =
            promotionDataSource.getCouponDetail(params.coupon, selectedItems.map { it.sku }, params.userToken)

        if (fullCart.checkValidPromotion(promotion)) {
            cartDataSource.saveCouponPromotion(cart.id, promotion)
            promotion
        } else {
            throw PromotionError.ApplyCouponError.InvalidCoupon(params.coupon)
        }
    }

    data class ApplyCouponParams(
        val coupon: String,
        val cartId: String?,
        val userId: String?,
        val userToken: String?
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId),
                StringValidator(coupon, "coupon", 1)
            )
    }

}