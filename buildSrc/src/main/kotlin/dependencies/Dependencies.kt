package dependencies

/**
 * Configuration version of all dependencies
 */

object DependenciesVersions {
    const val KOTLIN = "1.4.31"
    const val KOTLIN_COROUTINES = "1.4.3-native-mt"
    const val KOTLIN_SERIALIZATION = "1.1.0"
    const val KOTLIN_DATE_TIME = "0.1.1"
    const val JWT = "3.11.0"
    const val SPRINGDOC = "1.5.5"
    const val MICROMETER_REGISTRY_PROMETHEUS = "1.5.1"
    const val WIRE = "3.7.0"
    const val OKHTTP = "4.9.0"
    const val KTOR = "1.5.2"
    const val KODEIN_DI = "7.4.0"
    const val LOGGER = "1.2.3"
    const val SQL_DELIGHT = "1.4.4"
    const val FAST_ADAPTER = "5.3.5"
    const val COIL = "1.1.1"
    const val DAGGER_COMPILER = "2.26"
    const val LIFECYCLE = "2.4.0-alpha01"
    const val NAVIGATION = "2.3.4"
    const val FLOW_BINDING = "1.0.0"
}

/**
 * Project dependencies, makes it easy to include external binaries or
 * other library modules to build.
 */
object Dependencies {
    object Server {
        const val KOTLIN =
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${DependenciesVersions.KOTLIN}"
        const val KOTLIN_REFLECT =
            "org.jetbrains.kotlin:kotlin-reflect:${DependenciesVersions.KOTLIN}"
        const val KOTLIN_COROUTINES =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependenciesVersions.KOTLIN_COROUTINES}"
        const val KOTLIN_COROUTINES_REACTOR =
            "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${DependenciesVersions.KOTLIN_COROUTINES}"
        const val KOTLIN_SERIALIZATION =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${DependenciesVersions.KOTLIN_SERIALIZATION}"
        const val KOTLIN_DATE_TIME =
            "org.jetbrains.kotlinx:kotlinx-datetime:${DependenciesVersions.KOTLIN_DATE_TIME}"

        const val SPRING_WEBFLUX =
            "org.springframework.boot:spring-boot-starter-webflux"
        const val SPRING_BOOT_STARTER_ACTUATOR =
            "org.springframework.boot:spring-boot-starter-actuator"
        const val SPRINGDOC_OPENAPI_WEBFLUX_UI =
            "org.springdoc:springdoc-openapi-webflux-ui:${DependenciesVersions.SPRINGDOC}"
        const val MICROMETER_REGISTRY_PROMETHEUS =
            "io.micrometer:micrometer-registry-prometheus:${DependenciesVersions.MICROMETER_REGISTRY_PROMETHEUS}"


        const val SPRING_DATA_R2DBC =
            "org.springframework.boot:spring-boot-starter-data-r2dbc"
        const val SPRING_DATA_REDIS_REACTIVE =
            "org.springframework.boot:spring-boot-starter-data-redis-reactive"

        const val REACTOR_KOTLIN =
            "io.projectreactor.kotlin:reactor-kotlin-extensions"

        const val JWT = "com.auth0:java-jwt:${DependenciesVersions.JWT}"

        const val WIRE_CLIENT = "com.squareup.wire:wire-grpc-client:${DependenciesVersions.WIRE}"
        const val WIRE_KOTLIN_SERIALIZATION =
            "com.squareup.wire:wire-kotlin-serialization:${DependenciesVersions.WIRE}"
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${DependenciesVersions.OKHTTP}"
        const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${DependenciesVersions.OKHTTP}"
    }

    object Domain {
        const val KOTLIN_COROUTINES =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DependenciesVersions.KOTLIN_COROUTINES}"
        const val KOTLIN_SERIALIZATION =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${DependenciesVersions.KOTLIN_SERIALIZATION}"
        const val KOTLIN_DATE_TIME =
            "org.jetbrains.kotlinx:kotlinx-datetime:${DependenciesVersions.KOTLIN_DATE_TIME}"
    }

    object Core {
        const val KTOR_CLIENT = "io.ktor:ktor-client-core:${DependenciesVersions.KTOR}"
        const val KTOR_CLIENT_JSON = "io.ktor:ktor-client-json:${DependenciesVersions.KTOR}"
        const val KTOR_CLIENT_LOGGING = "io.ktor:ktor-client-logging:${DependenciesVersions.KTOR}"
        const val KTOR_CLIENT_SERIALIZATION =
            "io.ktor:ktor-client-serialization:${DependenciesVersions.KTOR}"
        const val KTOR_CLIENT_OKHTTP = "io.ktor:ktor-client-okhttp:${DependenciesVersions.KTOR}"
        const val KTOR_CLIENT_IOS = "io.ktor:ktor-client-ios:${DependenciesVersions.KTOR}"

        const val KODEIN_DI = "org.kodein.di:kodein-di:${DependenciesVersions.KODEIN_DI}"
        const val KODEIN_CONFIGURABLE =
            "org.kodein.di:kodein-di-conf:${DependenciesVersions.KODEIN_DI}"

        const val LOGGER = "ch.qos.logback:logback-classic:${DependenciesVersions.LOGGER}"
        const val SQL_DELIGHT =
            "com.squareup.sqldelight:runtime:${DependenciesVersions.SQL_DELIGHT}"
        const val SQL_DELIGHT_ANDROID =
            "com.squareup.sqldelight:android-driver:${DependenciesVersions.SQL_DELIGHT}"
        const val SQL_DELIGHT_IOS =
            "com.squareup.sqldelight:native-driver:${DependenciesVersions.SQL_DELIGHT}"
        const val SQL_DELIGHT_COROUTINE =
            "com.squareup.sqldelight:coroutines-extensions:${DependenciesVersions.SQL_DELIGHT}"

    }

    object Docs {
        const val SPRINGDOC_OPENAPI_KOTLIN =
            "org.springdoc:springdoc-openapi-kotlin:${DependenciesVersions.SPRINGDOC}"
    }

    object Android {
        const val FAST_ADAPTER = "com.mikepenz:fastadapter:${DependenciesVersions.FAST_ADAPTER}"
        const val FAST_ADAPTER_BINDING =
            "com.mikepenz:fastadapter-extensions-binding:${DependenciesVersions.FAST_ADAPTER}"
        const val FAST_ADAPTER_DIFF =
            "com.mikepenz:fastadapter-extensions-diff:${DependenciesVersions.FAST_ADAPTER}"
        const val FAST_ADAPTER_EXT =
            "com.mikepenz:fastadapter-extensions-ui:${DependenciesVersions.FAST_ADAPTER}"

        const val COIL = "io.coil-kt:coil:${DependenciesVersions.COIL}"

        // TODO : reuse Kodein
        const val DAGGER_COMPILER =
            "com.google.dagger:dagger-compiler:${DependenciesVersions.DAGGER_COMPILER}"
        const val DAGGER_PROCESSOR =
            "com.google.dagger:dagger-android-processor:${DependenciesVersions.DAGGER_COMPILER}"

        // lifecycle
        const val LIFECYCLE_VIEWMODEL =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${DependenciesVersions.LIFECYCLE}"
        const val LIFECYCLE_KTX =
            "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.LIFECYCLE}"

        const val NAVIGATION_FRAGMENT_KTX =
            "androidx.navigation:navigation-fragment-ktx:${DependenciesVersions.NAVIGATION}"

        // Flow Binding
        const val FLOW_BINDING_ANDROID =
            "io.github.reactivecircus.flowbinding:flowbinding-android:${DependenciesVersions.FLOW_BINDING}"
    }

}