plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization").version("1.5.0-RC")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
        getByName("test") {
            java.srcDir("src/test/kotlin")
        }
        getByName("androidTest") {
            java.srcDir("src/androidTest/kotlin")
        }
    }

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        exclude("META-INF/spring.tooling")
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/spring.handlers")
        exclude("META-INF/spring-configuration-metadata.json")
        exclude("META-INF/additional-spring-configuration-metadata.json")
        exclude("META-INF/spring.factories")
        exclude("META-INF/spring.schemas")
        exclude("META-INF/license.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(
        fileTree(
            mapOf(
                "dir" to "libs",
                "include" to listOf("*.jar")
            )
        )
    )

    // Terra Apollo
    implementation("vn.teko.apollo:terra-apollo:1.6.0")

    // Jetpack
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.4")

    // fast adapter
    implementation("com.mikepenz:fastadapter:5.3.5")
    implementation("com.mikepenz:fastadapter-extensions-binding:5.3.5")
    implementation("com.mikepenz:fastadapter-extensions-diff:5.3.5")
    implementation("com.mikepenz:fastadapter-extensions-ui:5.3.5")

    implementation("io.coil-kt:coil:1.1.1")

    implementation("io.ktor:ktor-client-okhttp:1.5.2")
    implementation("io.ktor:ktor-client-serialization:1.5.2")
    implementation("io.ktor:ktor-client-logging:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    // hilt
    implementation("com.google.dagger:hilt-android:2.33-beta")
    kapt("com.google.dagger:hilt-compiler:2.33-beta")

    // core
    implementation("vn.teko.terra:terra-core-android:0.6.2-alpha4")

    // cart
    implementation(project(":cart-ui-compose"))
    implementation("vn.teko.cart:cart-bus:9211184b") {
        exclude("org.springdoc")
    }
    implementation("androidx.appcompat:appcompat:1.3.0-rc01")

}