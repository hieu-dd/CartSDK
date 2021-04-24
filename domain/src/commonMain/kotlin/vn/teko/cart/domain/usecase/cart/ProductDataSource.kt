package vn.teko.cart.domain.usecase.cart

import vn.teko.cart.domain.exception.ExternalServiceError.DiscoveryError
import vn.teko.cart.domain.model.ProductEntity

/**
 * The *single source of truth* of the product
 *
 * This class will only handle raw product from a product data source
 *
 */
interface ProductDataSource {

    /**
     * Get products using its skus
     *
     * @param[skus] list of product skus
     *
     * @return all available [products][ProductEntity] based on its skus
     *
     * @throws [DiscoveryError.UnspecifiedError] [DiscoveryError.GetProductsError]
     *
     * Note: The source can return invalid products (non sellable, ...).
     * So make sure that you have filtered its out before using
     */
    suspend fun getValidProducts(skus: List<String>): List<ProductEntity>

}