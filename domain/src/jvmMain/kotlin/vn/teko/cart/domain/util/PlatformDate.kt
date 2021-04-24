package vn.teko.cart.domain.util

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

actual abstract class PlatformDateSerializer(val format: String) : KSerializer<Instant>

actual object RFC3339DateTimeSerializer : PlatformDateSerializer("yyyy-MM-dd'T'HH:mm:ssXXX") {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(
            DateTimeFormatter.ofPattern(format).format(value.toJavaInstant().atOffset(ZoneOffset.of("+7")))
        )
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return OffsetDateTime.parse(string.trim()).toInstant().toKotlinInstant()
    }

}


actual object ISO8601DateTimeSerializer : PlatformDateSerializer("yyyy-MM-dd'T'HH:mm:ss") {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(
            DateTimeFormatter.ofPattern(format).format(value.toJavaInstant().atOffset(ZoneOffset.of("+7")))
        )
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string.trim()).toInstant(ZoneOffset.of("+7")).toKotlinInstant()
    }

}