rootProject.name = "legends-tournaments-app"

include(
    "front-end-kv",
    "api"
)

val kotlinVersion: String by settings
val gradleVersionsVersion: String by settings
val shadowVersion: String by settings
val springDependencyManagementVersion: String by settings
val jibVersion: String by settings
val dockerRemoteAPIVersion: String by settings
val kotlinFrontendVersion: String by settings

pluginManagement {
    repositories {
//        mavenCentral()
//        jcenter()
//        maven { setUrl("https://jcenter.bintray.com/") }
//        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }

    resolutionStrategy {
        eachPlugin {
            when(requested.id.id) {
                "kotlin2js" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
                "kotlin-dce-js" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
                "org.jetbrains.kotlin.frontend" -> useModule("org.jetbrains.kotlin:kotlin-frontend-plugin:$kotlinFrontendVersion")
                "com.bmuschko.docker-remote-api" -> useVersion(dockerRemoteAPIVersion)
                "kotlinx-serialization" -> useModule("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.kapt" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.allopen" -> useVersion(kotlinVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
                "com.github.johnrengelman.shadow" -> useVersion(shadowVersion)
                "com.github.ben-manes.versions" -> useVersion(gradleVersionsVersion)
                "com.google.cloud.tools.jib" -> useVersion(jibVersion)
            }
        }
    }
}
