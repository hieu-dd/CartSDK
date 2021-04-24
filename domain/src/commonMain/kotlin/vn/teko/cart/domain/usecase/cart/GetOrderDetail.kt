package vn.teko.cart.domain.usecase.cart

import vn.teko.cart.domain.usecase.UseCase
import vn.teko.cart.domain.usecase.order.OrderDataSource

open class GetOrderDetail (
    private val orderDataSource: OrderDataSource
) : UseCase<GetOrderDetail.GetOrderDetailParams, OrderDataSource.OrderCaptureDetail>() {
    override suspend fun run(params: GetOrderDetailParams): OrderDataSource.OrderCaptureDetail {
        return orderDataSource.getOrderCaptureDetail(
            code = params.orderCode,
            userToken = params.userToken
        )
    }

    data class GetOrderDetailParams(
        val userToken: String,
        val orderCode: String
    ) : UseCase.Params()

}