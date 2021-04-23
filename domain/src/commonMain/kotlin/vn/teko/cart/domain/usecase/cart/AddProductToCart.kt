package vn.teko.cart.domain.usecase.cart

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import vn.teko.cart.domain.config.ApplyPromotionConfig
import vn.teko.cart.domain.exception.CartError
import vn.teko.cart.domain.extension.*
import vn.teko.cart.domain.model.CartEntity.Companion.MAX_ITEM_QUANTITY
import vn.teko.cart.domain.model.CartEntity.Companion.MIN_ITEM_QUANTITY
import vn.teko.cart.domain.model.CartItemEntity
import vn.teko.cart.domain.model.ProductEntity
import vn.teko.cart.domain.model.PromotionEntity
import vn.teko.cart.domain.usecase.NumberValidator
import vn.teko.cart.domain.usecase.NumberValidator.Companion.MIN_ID_VALUE
import vn.teko.cart.domain.usecase.StringValidator
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.promotion.GetDetailCoupon
import vn.teko.cart.domain.util.getOrElse

class AddProductToCart(
    private val productDataSource: ProductDataSource,
    private val cartDataSource: CartDataSource,
    private val importCart: ImportCart,
    private val getDetailCoupon: GetDetailCoupon
) : UseCase<AddProductToCart.AddProductToCartParams, CartItemEntity>() {
    override suspend fun run(params: AddProductToCartParams) = coroutineScope {

        val cart = cartDataSource.getCart(params.cartId, params.userId, createIfNotExists = true)

        val cartAsync = async {
            importCart(ImportCart.ImportCartParams(cart)).getOrElse { throw it }
        }
        val productsAsync = async {
            productDataSource.getValidProducts(listOf(params.sku))
        }

        val product = productsAsync.await().firstOrNull { it.productInfo.sku == params.sku }
            ?: throw CartError.ProductNotFound(params.sku)

        /**
         * Step3: Fetch detail promotion from product detail and validate promotion
         * If selected promotion is valid -> Add item with this promotion
         * Else -> throw an InapplicablePromotion exception
         */
        val promotion = product.promotions.firstOrNull { it.id == params.selectPromotionId }
        if (params.selectPromotionId != null && promotion == null) {
            throw CartError.AddToCartError.NonExistentPromotion
        }
        val quantity = params.quantity ?: 1

        if (promotion == null || validatePromotion(promotion, product)) {
            /**
             * If promotion is valid. Then build full cart data from raw cart data from DB
             * Then add new item to cart
             */
            val fullCart = cartAsync.await()

            /**
             * Add product to current cart
             */
            val item = fullCart.addItem(product, quantity, promotion, null, null)

            /**
             * Then validate the final quantity if it has exceeded the available number or MAX_ITEM_QUANTITY
             */
            validateFinalQuantity(item, params.minTotalAvailableToCheck)
            getDetailCoupon(GetDetailCoupon.GetCouponDetailParams(fullCart, params.userToken, cart.getCurrentCoupon()))
                .getOrNull()?.let { promotion ->
                    fullCart.applyCouponPromotion(promotion)
                }

            cartDataSource.saveCart(fullCart)

            item
        } else {
            throw CartError.AddToCartError.InapplicablePromotion
        }
    }

    private fun validateFinalQuantity(item: CartItemEntity, minTotalAvailableToCheck: Int) {
        item.product?.totalAvailable?.let { totalAvailable ->
            if (totalAvailable < minTotalAvailableToCheck && totalAvailable < item.quantity) {
                throw CartError.OutOfStock
            }
        }
        if (item.quantity > MAX_ITEM_QUANTITY) {
            throw CartError.ExceedMaxQuantity
        }
    }


    private fun validatePromotion(promotion: PromotionEntity, productEntity: ProductEntity): Boolean {
        return promotion.isValidForProduct(
            sellerId = productEntity.productInfo.seller.id,
            productQuantity = promotion.getBlockSize(),
            applyPromotionConfig = ApplyPromotionConfig()
        ) && promotion.isSelectionPromotionForProduct()
    }

    data class AddProductToCartParams(
        val cartId: String?,
        val sku: String,
        val quantity: Int?,
        val selectPromotionId: Int?,
        val userId: String?,
        val userToken: String?,
        val tenant: String,
        val minTotalAvailableToCheck: Int,
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = listOf(
                StringValidator(sku, "sku", null, null),
                NumberValidator(quantity, "quantity", MIN_ITEM_QUANTITY, MAX_ITEM_QUANTITY),
                NumberValidator(selectPromotionId, "selectPromotionId", MIN_ID_VALUE, Int.MAX_VALUE)
            )
    }
}