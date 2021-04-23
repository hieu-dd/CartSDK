package vn.teko.cart.docs.swagger.v3.oas.annotations

import vn.teko.cart.docs.swagger.v3.oas.annotations.extensions.Extension
import vn.teko.cart.docs.swagger.v3.oas.annotations.media.DiscriminatorMapping
import vn.teko.cart.docs.util.Void
import kotlin.reflect.KClass

expect annotation class Schema constructor(
    val implementation: KClass<*> = Void::class,
    val not: KClass<*> = Void::class,
    val oneOf: Array<KClass<*>> = [],
    val anyOf: Array<KClass<*>> = [],
    val allOf: Array<KClass<*>> = [],
    val name: String = "",
    val title: String = "",
    val multipleOf: Double = 0.0,
    val maximum: String = "",
    val exclusiveMaximum: Boolean = false,
    val minimum: String = "",
    val exclusiveMinimum: Boolean = false,
    val maxLength: Int = Int.MAX_VALUE,
    val minLength: Int = 0,
    val pattern: String = "",
    val maxProperties: Int = 0,
    val minProperties: Int = 0,
    val requiredProperties: Array<String> = [],
    val required: Boolean = false,
    val description: String = "",
    val format: String = "",
    val ref: String = "",
    val nullable: Boolean = false,
    val readOnly: Boolean = false,
    val writeOnly: Boolean = false,
    val accessMode: AccessMode = AccessMode.AUTO,
    val example: String = "",
    val externalDocs: ExternalDocumentation,
    val deprecated: Boolean = false,
    val type: String = "",
    val allowableValues: Array<String> = [],
    val defaultValue: String = "",
    val discriminatorProperty: String = "",
    val discriminatorMapping: Array<DiscriminatorMapping> = [],
    val hidden: Boolean = false,
    val enumAsRef: Boolean = false,
    val subTypes: Array<KClass<*>> = [],
    val extensions: Array<Extension> = []
)

expect enum class AccessMode {
    AUTO,
    READ_ONLY,
    WRITE_ONLY,
    READ_WRITE
}