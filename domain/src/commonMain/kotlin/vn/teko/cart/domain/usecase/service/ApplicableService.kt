package vn.teko.cart.domain.usecase.service

import kotlinx.serialization.Serializable
import vn.teko.cart.domain.model.OrderCapturePayload

@Serializable
class ApplicableService(
    val applicableSkus: List<String>,
    val service: Service,
    val serviceGroup: ServiceGroup?,
    val fee: Long,
    val isDefault: Boolean,
) {
    fun toAppliedService(): OrderCapturePayload.AppliedService = OrderCapturePayload.AppliedService(
        id = service.id,
        groupId = serviceGroup?.id,
        name = service.name,
        code = service.metaData?.providerServiceCode ?: "",
        fee = fee
    )
}
