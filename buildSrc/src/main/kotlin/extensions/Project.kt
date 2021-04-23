package extensions

import org.gradle.api.Project
/**
 * Obtain property declared on `$projectRoot/gitlab.properties` file.
 *
 * @param propertyName the name of declared property
 */
fun Project.getGitlabProperty(propertyName: String): String =
  utils.getGitlabProperty(this, propertyName)
