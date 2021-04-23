package vn.teko.cart.docs.swagger.v3.oas.annotations

import vn.teko.cart.docs.swagger.v3.oas.annotations.extensions.Extension
import vn.teko.cart.docs.swagger.v3.oas.annotations.media.DiscriminatorMapping
import vn.teko.cart.docs.util.Void
import kotlin.reflect.KClass


actual annotation class Schema(
    actual val implementation: KClass<*> = Void::class,
    actual val not: KClass<*> = Void::class,
    actual val oneOf: Array<KClass<*>> = [],
    actual val anyOf: Array<KClass<*>> = [],
    actual val allOf: Array<KClass<*>> = [],
    actual val name: String = "",
    actual val title: String = "",
    actual val multipleOf: Double = 0.0,
    actual val maximum: String = "",
    actual val exclusiveMaximum: Boolean = false,
    actual val minimum: String = "",
    actual val exclusiveMinimum: Boolean = false,
    actual val maxLength: Int = Int.MAX_VALUE,
    actual val minLength: Int = 0,
    actual val pattern: String = "",
    actual val maxProperties: Int = 0,
    actual val minProperties: Int = 0,
    actual val requiredProperties: Array<String> = [],
    actual val required: Boolean = false,
    actual val description: String = "",
    actual val format: String = "",
    actual val ref: String = "",
    actual val nullable: Boolean = false,
    actual val readOnly: Boolean = false,
    actual val writeOnly: Boolean = false,
    actual val accessMode: AccessMode = AccessMode.AUTO,
    actual val example: String = "",
    actual val externalDocs: ExternalDocumentation,
    actual val deprecated: Boolean = false,
    actual val type: String = "",
    actual val allowableValues: Array<String> = [],
    actual val defaultValue: String = "",
    actual val discriminatorProperty: String = "",
    actual val discriminatorMapping: Array<DiscriminatorMapping> = [],
    actual val hidden: Boolean = false,
    actual val enumAsRef: Boolean = false,
    actual val subTypes: Array<KClass<*>> = [],
    actual val extensions: Array<Extension> = []
)

actual enum class AccessMode {
    AUTO, READ_ONLY, WRITE_ONLY, READ_WRITE
}