package extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.credentials.HttpHeaderCredentials
import org.gradle.authentication.http.HttpHeaderAuthentication
import java.net.URI

/**
 * Adds all default repositories used to access to the different declared dependencies
 */
fun RepositoryHandler.applyDefault() {
    google()
    jcenter()
    mavenCentral()
    maven {
        url = URI.create("https://kotlin.bintray.com/kotlinx/")
    }
}

fun RepositoryHandler.gitlabTeko(project: Project, uri: String? = null) {
    maven {
        name = "TekoGitlabPackages"
        url = project.uri(uri ?: "https://git.teko.vn/api/v4/groups/924/-/packages/maven")

        credentials(HttpHeaderCredentials::class.java) {
            val privateToken = try {
                project.getGitlabProperty("privateToken")
            } catch (error: NoSuchFieldException) {
                System.getenv("GITLAB_PRIVATE_TOKEN")
            }

            if (privateToken != null) {
                name = "Private-Token"
                value = privateToken
            } else {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }

        }
        authentication {
            create("header", HttpHeaderAuthentication::class.java)
        }
    }
}