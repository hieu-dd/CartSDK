package vn.teko.cart.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vn.teko.cart.docs.swagger.v3.oas.annotations.ExternalDocumentation
import vn.teko.cart.docs.swagger.v3.oas.annotations.Schema
import vn.teko.cart.domain.extension.validate
import vn.teko.cart.domain.model.CartPromotionEntity.*
import vn.teko.cart.domain.usecase.EmailValidator
import vn.teko.cart.domain.usecase.PhoneNumberValidator
import vn.teko.cart.domain.usecase.StringValidator
import vn.teko.cart.domain.usecase.Validator
import vn.teko.cart.domain.usecase.Validator.Companion.EMAIL_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.INVOICE_ADDRESS_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.INVOICE_NAME_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.INVOICE_TAX_CODE_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.NOTE_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.STRING_MIN_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.TELEPHONE_MAX_LENGTH
import vn.teko.cart.domain.usecase.Validator.Companion.TELEPHONE_MIN_LENGTH
import vn.teko.cart.domain.util.ISO8601DateTimeSerializer
import vn.teko.cart.domain.util.RFC3339DateTimeSerializer

@Serializable
data class OrderCapturePayload(
    val customer: Customer,
    val items: List<Item>,
    val notes: Map<Int, String>? = null,
    val note: String? = null,
    val billingInfo: BillingInfo? = null,
    val shippingInfo: ShippingInfo?,
    val delivery: Boolean,
    val referralCode: String? = null,
    val grandTotal: Double,
    val promotions: List<Promotion>? = null,
    val terminalCode: String,
    val channelCode: String,
    val channelType: String,
    val channelId: Int,
    val services: List<AppliedService>? = null,
    @Serializable(with = RFC3339DateTimeSerializer::class)
    val expiryTime: Instant? = null,
) {
    @Serializable
    data class Customer(
        val id: String,
        var name: String,
        val email: String? = null,
        val phone: String,
        val address: String? = null,
        val wardId: String? = null,
        val districtId: String? = null,
        val provinceId: String? = null,
        val asiaCrmId: String? = null,
        val fullAddress: String,
    ) {
        init {
            mutableListOf<Validator>(
                PhoneNumberValidator(phone, true),
                StringValidator(phone, "customer.phone", TELEPHONE_MIN_LENGTH, TELEPHONE_MAX_LENGTH),
            ).apply {
                if (!email.isNullOrBlank()) addAll(
                    listOf(
                        EmailValidator(email, false),
                        StringValidator(email, "customer.email", STRING_MIN_LENGTH, EMAIL_MAX_LENGTH),
                    )
                )
            }.validate()
        }
    }

    @Serializable
    data class Item(
        val lineItemId: String,
        val sku: String,
        val name: String,
        val displayName: String,
        val quantity: Int,
        val uom: String? = null,
        val bizType: String,
        val unitPrice: Long,
        val unitDiscount: Long,
        val discountReason: String? = null,
        val unitAdd: Long,
        val addReason: String? = null,
        val price: Long,
        val warranty: Int,
        val rowTotal: Long,
        val sellerId: Int,
        val parentSku: String? = null,
        val installation: Boolean? = null,
        val technicalSupport: Boolean? = null,
        val addedPrice: Long? = null,
        val consultationServiceFee: Double = 0.0
    ) {
        // use 10 as default value for now
        var vatRate: Long = 10
        var taxOutCode: String = "10"

        companion object {
            fun from(item: CartItemEntity): Item {
                return with(item) {
                    Item(
                        lineItemId = lineItemId,
                        sku = sku,
                        name = name,
                        displayName = displayName,
                        quantity = quantity,
                        bizType = "Biz",
                        unitPrice = unitPrice,
                        unitDiscount = unitDiscount,
                        unitAdd = unitAdd,
                        price = price,
                        warranty = product?.productInfo?.warranty?.months?.toInt() ?: 0,
                        rowTotal = rowTotal,
                        sellerId = sellerId,
                        consultationServiceFee = item.consultationServiceFee
                    ).apply {
                        product?.productInfo?.tax?.let { tax ->
                            tax.taxOut?.let { vatRate = it }
                            tax.taxOutCode?.let { taxOutCode = it }
                        }
                    }
                }
            }
        }
    }

    @Serializable
    data class Note(
        val sellerId: Int,
        val value: String
    ) {
        init {
            listOf<Validator>(
                StringValidator(value, "note.value", STRING_MIN_LENGTH, NOTE_MAX_LENGTH, false)
            ).validate()
        }
    }

    @Serializable
    data class BillingInfo(
        val name: String,
        val address: String,
        val email: String? = null,
        val taxCode: String,
        val phone: String? = null,
        val type: String,
        val printAfter: String,
        val printPretaxPrice: Boolean = true,
        val objectId: String? = null
    ) {
        companion object {
            fun from(invoice: InvoiceRequest?): BillingInfo? {
                if (invoice == null) return null
                invoice.run {
                    return BillingInfo(
                        name = name,
                        email = email,
                        phone = phone,
                        taxCode = taxCode,
                        address = address,
                        type = "01",
                        printAfter = "0",
                        printPretaxPrice = false
                    )
                }
            }
        }
    }

    @Serializable
    data class ShippingInfo(
        val name: String? = null,
        val phone: String? = null,
        val address: String? = null,
        val fullAddress: String? = null,
        val wardId: String? = null,
        val districtId: String? = null,
        val provinceId: String? = null,
        val email: String? = null,
        val note: String? = null,
        val country: String? = null,
        val type: Int? = null,
        val latitude: Float? = null,
        val longitude: Float? = null,
        val storeCode: String? = null,
        val expectedDate: String? = null,
        @Serializable(with = ISO8601DateTimeSerializer::class)
        val expectedReceiveFrom: Instant? = null,
        @Serializable(with = ISO8601DateTimeSerializer::class)
        val expectedReceiveTo: Instant? = null
    ) {
        companion object {
            fun from(shippingInfo: ShippingInfoEntity, additionalInfo: AdditionalShippingInfo?): ShippingInfo {
                shippingInfo.run {
                    return ShippingInfo(
                        name = name,
                        phone = telephone,
                        address = address,
                        fullAddress = listOf(
                            address,
                            wardName,
                            districtName,
                            provinceName
                        ).filterNot { it.isNullOrBlank() }.joinToString(separator = ", "),
                        wardId = wardCode,
                        districtId = districtCode,
                        provinceId = provinceCode,
                        email = email,
                        note = addressNote,
                        longitude = longitude,
                        latitude = latitude,
                        storeCode = additionalInfo?.storeCode,
                        expectedDate = additionalInfo?.expectedDate,
                        expectedReceiveFrom = additionalInfo?.expectedReceiveFrom,
                        expectedReceiveTo = additionalInfo?.expectedReceiveTo
                    )
                }
            }
        }
    }

    @Serializable
    data class AdditionalShippingInfo(
        val storeCode: String? = null,
        @Deprecated("Only for backward compatible, will be removed")
        @Schema(
            example = "2021-01-02T15:04:05",
            externalDocs = ExternalDocumentation(),
            deprecated = true
        )
        val expectedDate: String? = null,
        @Serializable(with = RFC3339DateTimeSerializer::class)
        val expectedReceiveFrom: Instant? = null,
        @Serializable(with = RFC3339DateTimeSerializer::class)
        val expectedReceiveTo: Instant? = null
    )

    @Serializable
    data class Promotion(
        val id: Int,
        val promotionId: String,
        val type: String,
        val applyType: String,
        val applyOn: List<ApplyOn>,
        val discount: Double = 0.0,
        val originalDiscount: Double? = null,
        val gifts: List<Gift>,
        val removedGifts: List<RemovedGift>? = null,
        val quantity: Int,
        val voucher: Voucher? = null,
        val coupon: String?,
        val sellerIds: List<Int>
    ) {
        companion object {
            fun from(promotion: CartPromotionEntity): Promotion {
                promotion.run {
                    return Promotion(
                        id = promotionId,
                        promotionId = id,
                        type = type,
                        applyType = applyType,
                        applyOn = applyOn.map { ApplyOn.from(it) },
                        discount = discount,
                        gifts = gifts.map { Gift.from(it) },
                        quantity = quantity,
                        voucher = Voucher.from(voucher),
                        coupon = coupon,
                        sellerIds = sellerIds
                    )
                }
            }
        }
    }

    @Serializable
    data class ApplyOn(
        val lineItemId: String,
        val quantity: Int
    ) {
        companion object {
            fun from(apply: ApplyEntity): ApplyOn {
                apply.run {
                    return ApplyOn(
                        lineItemId = lineItemId,
                        quantity = quantity
                    )
                }
            }
        }
    }

    @Serializable
    data class Gift(
        val lineItemId: String,
        val sku: String,
        val quantity: Int,
        val name: String
    ) {
        companion object {
            fun from(gift: GiftEntity): Gift {
                gift.run {
                    return Gift(
                        lineItemId = lineItemId,
                        quantity = quantity,
                        sku = sku,
                        name = name
                    )
                }
            }
        }
    }

    @Serializable
    data class RemovedGift(
        val sku: String,
        val quantity: Int
    )

    @Serializable
    data class Voucher(
        val quantity: Int
    ) {
        companion object {
            fun from(voucher: VoucherEntity?): Voucher? {
                if (voucher == null) return null
                voucher.run {
                    return Voucher(
                        quantity = quantity
                    )
                }
            }
        }
    }

    @Serializable
    data class ServiceFee(
        val delivery: List<Delivery>
    ) {
        companion object {
            fun from(shippingFee: ShippingFeeEntity): ServiceFee? {
                shippingFee.run {
                    return when {
                        shippingFee.shippingFee > 0 -> ServiceFee(
                            delivery = listOf(
                                Delivery(
                                    name = serviceCode,
                                    price = shippingFee.shippingFee.toDouble(),
                                    discountAmount = 0.0,
                                    rowTotal = shippingFee.shippingFee.toDouble(),
                                    sellerId = null
                                )
                            )
                        )
                        else -> null
                    }
                }
            }
        }
    }

    @Serializable
    data class Delivery(
        val name: String,
        val price: Double,
        val discountAmount: Double,
        val rowTotal: Double,
        val sellerId: Int?
    )

    @Serializable
    data class AppliedService(
        val id: Int,
        val groupId: Int? = null,
        val name: String,
        val code: String? = null,
        val fee: Long
    )

    @Serializable
    data class InvoiceRequest(
        val name: String,
        val email: String? = null,
        val phone: String? = null,
        val taxCode: String,
        val address: String
    ) {
        init {
            mutableListOf<Validator>(
                StringValidator(name, "invoiceInfo.name", STRING_MIN_LENGTH, INVOICE_NAME_MAX_LENGTH),
                StringValidator(taxCode, "invoiceInfo.taxCode", STRING_MIN_LENGTH, INVOICE_TAX_CODE_MAX_LENGTH),
                StringValidator(address, "invoiceInfo.address", STRING_MIN_LENGTH, INVOICE_ADDRESS_MAX_LENGTH),
            ).apply {
                if (!email.isNullOrBlank()) addAll(
                    listOf(
                        EmailValidator(email, false),
                        StringValidator(email, "invoiceInfo.email", STRING_MIN_LENGTH, EMAIL_MAX_LENGTH),
                    )
                )
                if (!phone.isNullOrBlank()) addAll(
                    listOf(
                        PhoneNumberValidator(phone, false),
                        StringValidator(phone, "invoiceInfo.phone", TELEPHONE_MIN_LENGTH, TELEPHONE_MAX_LENGTH),
                    )
                )
            }.validate()
        }
    }
}

