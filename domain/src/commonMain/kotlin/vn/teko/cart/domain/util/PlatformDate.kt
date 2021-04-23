package vn.teko.cart.domain.util

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

expect abstract class PlatformDateSerializer : KSerializer<Instant>

expect object RFC3339DateTimeSerializer : PlatformDateSerializer

expect object ISO8601DateTimeSerializer : PlatformDateSerializer

object PlatformDateFromTimeStampSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.epochSeconds.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return Instant.fromEpochMilliseconds(string.toLong() * 1000)
    }
}