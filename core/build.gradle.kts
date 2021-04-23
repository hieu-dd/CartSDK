import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import dependencies.Dependencies
import dependencies.TestDependencies

plugins {
    kotlin("multiplatform")
    id(BuildPlugins.KOTLIN_PLUGIN_SERIALIZATION)
    id("com.android.library")
    id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    ios {
        binaries {
            framework {
                baseName = "core"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":domain"))
                implementation(Dependencies.Core.SQL_DELIGHT_COROUTINE)
                implementation(Dependencies.Core.SQL_DELIGHT)
                implementation(Dependencies.Core.KODEIN_DI)
                implementation(Dependencies.Core.KODEIN_CONFIGURABLE)
                implementation(Dependencies.Core.KTOR_CLIENT)
                implementation(Dependencies.Core.KTOR_CLIENT_JSON)
                implementation(Dependencies.Core.KTOR_CLIENT_LOGGING)
                implementation(Dependencies.Core.KTOR_CLIENT_SERIALIZATION)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(TestDependencies.Core.KTOR_CLIENT_MOCK)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Core.LOGGER)
                implementation(Dependencies.Core.SQL_DELIGHT_ANDROID)
                implementation(Dependencies.Core.KTOR_CLIENT_OKHTTP)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(TestDependencies.Core.ANDROID_TEST_CORE)
                implementation(TestDependencies.Core.ANDROID_TEST_EXT)
                implementation(TestDependencies.Core.ROBOLECTRIC)
                implementation(TestDependencies.Core.KOTLIN_COROUTINES_TEST)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Dependencies.Core.SQL_DELIGHT_IOS)
                implementation(Dependencies.Core.KTOR_CLIENT_IOS)
            }
        }
        val iosTest by getting
    }
}

sqldelight {
    database("CartDB") {
        packageName = "vn.teko.cart.core.db"
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)