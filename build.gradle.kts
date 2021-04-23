import org.gradle.kotlin.dsl.support.compileKotlinScriptModuleTo

// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
    repositories {
        mavenCentral()
        maven {
            url = java.net.URI.create("https://maven.google.com")
        }
        jcenter()
    }
}

buildscript {
    val compose_version by extra("1.0.0-beta01")
    repositories {
        google()
        mavenCentral()
        maven {
            url = java.net.URI.create("https://maven.google.com")
        }
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha14")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath( "com.google.dagger:hilt-android-gradle-plugin:2.33-beta")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

