package vn.teko.cart.domain.usecase.service

import vn.teko.cart.domain.exception.ExternalServiceError.ServiceError
import vn.teko.cart.domain.model.CartEntity


/**
 * The *single source of truth* of the service
 *
 * This class will only handle raw service from a service data source
 *
 */
interface ServiceDataSource {
    companion object{
        const val DELIVERY_SERVICE_GROUP = 1
    }
    /**
     * Get applicable services from policy v2
     * @param[terminalCode] the terminal code
     * @param[shippingWardCode] the shipping information
     * @param[cart] current cart
     *
     * @return list [ApplicableService] are services cart can use
     *
     */
    suspend fun getApplicableServices(
        terminalCode: String,
        shippingWardCode: String,
        cart: CartEntity
    ): List<ApplicableService>

    /**
     * Get all delivery services that can be applied on current cart
     *
     * @param[terminalCode] the terminal code
     * @param[shippingWardCode] the ward code of shipping location
     * @param[cart] current cart
     *
     * @return all available [delivery services][Service] that can be applied on current cart
     *
     * @throws [ServiceError.UnspecifiedError], [ServiceError.GetServiceError]
     */
    suspend fun getDeliveryServices(
        terminalCode: String,
        shippingWardCode: String,
        cart: CartEntity,
    ): List<ApplicableService>

    /**
     * Get default delivery service of the current cart
     *
     * @param[terminalCode] the terminal code
     * @param[shippingWardCode] the ward code of shipping location
     * @param[cart] current cart
     *
     * @return the default [delivery service][Service] that can be applied on current cart
     *
     * @throws [ServiceError.UnspecifiedError], [ServiceError.GetServiceError]
     */
    suspend fun getDefaultDeliveryService(
        terminalCode: String,
        shippingWardCode: String,
        cart: CartEntity,
    ): ApplicableService
}