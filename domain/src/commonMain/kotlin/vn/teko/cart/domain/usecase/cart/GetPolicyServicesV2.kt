package vn.teko.cart.domain.usecase.cart

import vn.teko.cart.domain.exception.ExternalServiceError
import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.service.ApplicableService
import vn.teko.cart.domain.usecase.service.ServiceDataSource
import vn.teko.cart.domain.util.getOrElse

class GetPolicyServicesV2(
    private val getCart: GetCart,
    private val serviceDataSource: ServiceDataSource
) : UseCase<GetPolicyServicesV2.GetPolicyServicesV2Params, List<ApplicableService>>() {

    override suspend fun run(params: GetPolicyServicesV2Params): List<ApplicableService> {
        val cart = getCart(
            GetCart.GetCartParam(
                cartId = params.cartId,
                terminalCode = params.terminalCode,
                userId = params.userId,
                userToken = params.userToken
            )
        ).getOrElse { throw it }

        val shippingLocation = cart.shippingLocation ?: throw ExternalServiceError.ServiceError.ShippingInfoRequired

        return serviceDataSource.getApplicableServices(
            terminalCode = params.terminalCode,
            cart = cart,
            shippingWardCode = shippingLocation
        )
    }

    data class GetPolicyServicesV2Params(
        val terminalCode: String,
        val cartId: String?,
        val userId: String?,
        val userToken: String?
    ) : UseCase.Params() {
        override val validators: List<Validator>
            get() = emptyList()
    }

}