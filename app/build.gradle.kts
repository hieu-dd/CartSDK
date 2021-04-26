plugins {
    id("com.android.application")
    id( "org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id( "org.jetbrains.kotlin.plugin.serialization").version("1.5.0-RC")
}

android {
    kapt { generateStubs = true }

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

    buildTypes {

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true

    }
    buildFeatures {
        compose = true
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
    implementation("vn.teko.cart:cart-bus:experimental")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.0-alpha06")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
    implementation("com.google.dagger:hilt-android:2.33-beta")
    kapt("com.google.dagger:hilt-compiler:2.33-beta")
    implementation("io.ktor:ktor-client-okhttp:1.5.2")
    implementation("io.ktor:ktor-client-serialization:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("io.ktor:ktor-client-logging:1.5.2")
    implementation ("com.google.accompanist:accompanist-coil:0.7.1")
    implementation( "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha03")
    implementation ("androidx.compose.compiler:compiler:1.0.0-beta05")
    implementation ("androidx.activity:activity-compose:1.0.0-beta05")

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=org.mylibrary.OptInAnnotation"
}