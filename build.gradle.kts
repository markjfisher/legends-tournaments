plugins {
    id("com.github.ben-manes.versions")
    kotlin("jvm") apply false
    id("kotlin2js") apply false
}

tasks {
    getByName<Wrapper>("wrapper") {
        gradleVersion = "5.4"
        distributionType = Wrapper.DistributionType.ALL
    }
}

defaultTasks(
        "clean", "build"
)

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap/")
        }

        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://jcenter.bintray.com/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }

    group = "net.markjfisher"
    version = "1.0.0"
}
