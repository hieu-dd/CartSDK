// Top-level build file where you can add configuration options common to all sub-projects/modules
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
buildscript {
    val compose_version by extra("1.0.0-beta05")
    repositories {
        google()
        mavenCentral()
//        maven {
//            url = java.net.URI.create("https://maven.google.com")
//        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha14")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.33-beta")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}


