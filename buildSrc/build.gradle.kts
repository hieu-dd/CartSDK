plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

object PluginsVersions {
    const val KOTLIN = "1.4.31"
    const val GRADLE_SPRING_BOOT = "2.4.5"
    const val GRADLE_SPRING_DM = "1.0.11.RELEASE"
    const val GRADLE_ANDROID = "4.1.0"
    const val GRADLE_SQL_DELIGHT = "1.4.4"
    const val GRADLE_WIRE = "3.7.0"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginsVersions.KOTLIN}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${PluginsVersions.KOTLIN}")
    implementation("org.jetbrains.kotlin:kotlin-serialization:${PluginsVersions.KOTLIN}")

    implementation("org.springframework.boot:spring-boot-gradle-plugin:${PluginsVersions.GRADLE_SPRING_BOOT}")
    implementation("io.spring.gradle:dependency-management-plugin:${PluginsVersions.GRADLE_SPRING_DM}")

    implementation("com.android.tools.build:gradle:${PluginsVersions.GRADLE_ANDROID}")

    implementation("com.squareup.sqldelight:gradle-plugin:${PluginsVersions.GRADLE_SQL_DELIGHT}")

    implementation("com.squareup.wire:wire-gradle-plugin:${PluginsVersions.GRADLE_WIRE}")
}
