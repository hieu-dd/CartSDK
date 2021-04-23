plugins {
    kotlin("multiplatform")
    id( "org.jetbrains.kotlin.plugin.serialization").version("1.5.0-RC")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    ios {
        binaries {
            framework {
                baseName = "docs"
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val jvmMain by getting {
            dependencies {
                api("org.springdoc:springdoc-openapi-kotlin:1.5.5")
            }
        }
        val iosMain by getting
    }
}
