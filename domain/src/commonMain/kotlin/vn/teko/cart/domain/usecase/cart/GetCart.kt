package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.extension.*
import vn.teko.cart.domain.model.CartEntity
import vn.teko.cart.domain.usecase.CartValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.loyalty.LoyaltyDataSource
import vn.teko.cart.domain.usecase.promotion.GetDetailCoupon
import vn.teko.cart.domain.usecase.promotion.PromotionDataSource
import vn.teko.cart.domain.usecase.service.ServiceDataSource
import vn.teko.cart.domain.util.getOrElse

open class GetCart(
    private val cartDataSource: CartDataSource,
    private val promotionDataSource: PromotionDataSource,
    private val importCart: ImportCart,
    private val getDetailCoupon: GetDetailCoupon,
    private val serviceDataSource: ServiceDataSource,
    private val loyaltyDataSource: LoyaltyDataSource
) : UseCase<GetCart.GetCartParam, CartEntity>() {

    override suspend fun run(params: GetCartParam) = coroutineScope {
        /**
         * Step 1: Find cart in DB by CartId or UserId
         */
        val cart = cartDataSource.getCart(params.cartId, params.userId, createIfNotExists = true)
        val fullCartAsync = async {
            importCart(ImportCart.ImportCartParams(cart, params.isCheckout))
        }

        /**
         * Fetch orderPromotions
         */
        val orderPromotionsAsync = async {
            promotionDataSource.getOrderPromotions(cart.items.filter { !it.isGift && it.isSelected }.map { it.sku })
        }

        /**
         * Apply coupon if exists
         */
        val fullCart = fullCartAsync.await().getOrElse { throw it }

        /**
         * Fetch and apply coupon
         */
        getDetailCoupon(GetDetailCoupon.GetCouponDetailParams(fullCart, params.userToken, cart.getCurrentCoupon()))
            .getOrNull()?.let { promotion ->
                fullCart.applyCouponPromotion(promotion)
            }

        /**
         * Then apply best order promotion
         */
        val orderPromotions = orderPromotionsAsync.await()
        fullCart.applyBestOrderPromotions(orderPromotions.filter { it.isOrderPromotion() })
        fullCart.updateOrderCoupons(orderPromotions.filter { it.isCouponPromotion() })


        val shippingInfo = cartDataSource.getShippingInfo(cart.id)

        /**
         * cart v1 require wardCode when add shipping request but we don't
         * so when wardCode is null, we save to db blank, and when get to
         * call policies service, if wardCode is null or blank, we use districtCode instead
         */
        val shippingLocation = shippingInfo?.run {
            if (wardCode.isNullOrBlank()) districtCode else wardCode
        }

        /**
         * Get shipping fee and service code from Policies service
         */
        val deliveryService = async {
            if (shippingLocation != null && fullCart.items.any { it.isSelected }) {
                serviceDataSource.getDefaultDeliveryService(params.terminalCode, shippingLocation, fullCart)
            } else {
                null
            }
        }

        /**
         * Pre calculate dino point from Loyalty Service
         */
        val preCalculatePointsAsync = async {
            if (fullCart.grandTotal == 0L || params.merchantCode.isNullOrBlank()) {
                null
            } else {
                loyaltyDataSource.preCalculatePoints(
                    merchantCode = params.merchantCode,
                    cart = fullCart
                )
            }
        }

        fullCart.apply {
            deliveryService.await()?.let {
                this.shippingFee = it.fee
                this.grandTotal += it.fee
                this.serviceCode = it.service.metaData?.providerServiceCode.orEmpty()
                this.defaultDeliveryService = it.toAppliedService()
            }
            preCalculatedPoints = preCalculatePointsAsync.await()?.points
        }

        cartDataSource.saveCart(fullCart)
        fullCart.apply {
            isNew = false
            this.shippingLocation = shippingLocation
        }
    }

    data class GetCartParam(
        val cartId: String?,
        val userId: String?,
        val userToken: String?,
        val terminalCode: String,
        val isCheckout: Boolean = false,
        val merchantCode: String? = null
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                CartValidator(cartId, userId)
            )
    }
}