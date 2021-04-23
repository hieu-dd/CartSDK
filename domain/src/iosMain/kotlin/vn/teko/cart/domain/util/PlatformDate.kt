package vn.teko.cart.domain.util

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toNSDate


actual abstract class PlatformDateSerializer(val format: String) : KSerializer<Instant>

actual object RFC3339DateTimeSerializer : PlatformDateSerializer("yyyy-MM-dd'T'HH:mm:ssXXX") {
    private val formatter = NSDateFormatter().apply {
        locale = NSLocale("vi_VN")
        dateFormat = format
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(formatter.stringFromDate(value.toNSDate()))
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return formatter.dateFromString(string)!!.toKotlinInstant()
    }
}


actual object ISO8601DateTimeSerializer : PlatformDateSerializer("yyyy-MM-dd'T'HH:mm:ss") {
    private val formatter = NSDateFormatter().apply {
        locale = NSLocale("vi_VN")
        dateFormat = format
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(formatter.stringFromDate(value.toNSDate()))
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return formatter.dateFromString(string)!!.toKotlinInstant()
    }
}
