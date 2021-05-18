// Top-level build file where you can add configuration options common to all sub-projects/modules
allprojects {
    repositories {
        google()
        jcenter() // TODO remove jcenter because it will be shut down soon
        mavenCentral()
        maven {
            url = java.net.URI.create("https://kotlin.bintray.com/kotlinx/")
        }
        // All packages from Terra Platform
        maven {
            name = "TekoGitlabPackages"
            url = project.uri("https://git.teko.vn/api/v4/groups/924/-/packages/maven")

            credentials(HttpHeaderCredentials::class.java) {
                val privateToken = "hcmDJjULKfVA9jvaTFpH"
                name = "Private-Token"
                value = privateToken
            }
            authentication {
                create("header", HttpHeaderAuthentication::class.java)
            }
        }

        maven {
            name = "TekoGitlabPackages"
            url = project.uri("https://git.teko.vn/api/v4/projects/1745/packages/maven")

            credentials(HttpHeaderCredentials::class.java) {
                val privateToken = "hcmDJjULKfVA9jvaTFpH"
                name = "Private-Token"
                value = privateToken
            }
            authentication {
                create("header", HttpHeaderAuthentication::class.java)
            }
        }
    }
}
buildscript {
     val compose_version by extra("1.0.0-beta05")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.33-beta")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}


