rootProject.name = "legends-tournaments-app"

include(
    "api"
)

val kotlinVersion: String by settings
val gradleVersionsVersion: String by settings
val shadowVersion: String by settings
val springDependencyManagementVersion: String by settings

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when(requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.kapt" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.allopen" -> useVersion(kotlinVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
                "com.github.johnrengelman.shadow" -> useVersion(shadowVersion)
                "com.github.ben-manes.versions" -> useVersion(gradleVersionsVersion)
            }
        }
    }
}
