package utils

import org.gradle.api.Project
import java.util.*

private const val GITLAB_PROPERTIES_FILE_NAME = "gitlab.properties"

/**
 * Util to obtain property declared on `$projectRoot/gitlab.properties` file.
 *
 * @param project the project reference
 * @param propertyName the name of declared property
 *
 * @return the value of property name, otherwise throw [Exception]
 */
fun getGitlabProperty(project: Project, propertyName: String): String {
    return getPropertyFromFile(project, GITLAB_PROPERTIES_FILE_NAME, propertyName)
}

fun getPropertyFromFile(project: Project, fileName: String, propertyName: String): String {
    val properties = Properties().apply {
        val propertiesFile = project.rootProject.file(fileName)
        if (propertiesFile.exists()) {
            load(propertiesFile.inputStream())
        }
    }

    return properties.getProperty(propertyName)
        ?: throw NoSuchFieldException("Not defined property: $propertyName")
}
