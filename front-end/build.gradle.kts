import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerRemoveContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStopContainer
import com.bmuschko.gradle.docker.tasks.image.*

plugins {
    id("kotlin2js")
    id("kotlinx-serialization")
    id("com.bmuschko.docker-remote-api")
}

val kotlinVersion: String by project
val kotlinxCoroutinesVersion: String by project
val kotlinxSerializationRuntimeVersion: String by project
val kunafaVersion: String by project
val korioJsVersion: String by project

dependencies {
    implementation(kotlin("stdlib-js"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinxCoroutinesVersion")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$kotlinxCoroutinesVersion")

    compile("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$kotlinxSerializationRuntimeVersion")

    implementation("com.narbase:kunafa:$kunafaVersion")

    // compileOnly("com.bmuschko:gradle-docker-plugin:4.8.1")

    // waiting for korio to support kotlin 1.3.3x
    // implementation("com.soywiz:korio-js:$korioJsVersion")

    testImplementation(kotlin("stdlib-js"))

    // java only! not js
    // testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
}

tasks {
    val copyWebDir by registering(Copy::class) {
        group = "build"
        dependsOn(build)
        from("$buildDir/web")
        into("$buildDir/docker")
    }

    val createDockerFile by registering(Dockerfile::class) {
        group = "build"
        dependsOn(copyWebDir)
        from("nginx:alpine")
        copyFile(".", "/usr/share/nginx/html")
        label(mapOf("maintainer" to "Mark Fisher 'mark.j.fisher@gmail.com'"))
    }

    val createDockerImage by registering(DockerBuildImage::class) {
        group = "build"
        dependsOn(createDockerFile)
        tags.add("markjfisher/legends-tournaments-frontend:latest")
    }

    val removeDockerContainer by registering(DockerRemoveContainer::class) {
        targetContainerId("tournament-front-end")
        onError {
            if (!this.message!!.contains("No such container"))
                throw this
        }
    }

    val createDockerContainer by registering(DockerCreateContainer::class) {
        group = "build"
        dependsOn(removeDockerContainer)
        dependsOn(createDockerImage)
        targetImageId(createDockerImage.get().imageId)
        containerName.set("tournament-front-end")
        portBindings.set(listOf("8888:80"))
        autoRemove.set(false)
    }

    val startDockerContainer by registering(DockerStartContainer::class) {
        group = "docker"
        dependsOn(createDockerContainer)
        targetContainerId(createDockerContainer.get().containerId)
    }

    val stopDockerContainer by registering(DockerStopContainer::class) {
        // This only works when running entire functionTestApp task, as the containerId is not known otherwise
        group = "docker"
        targetContainerId(createDockerContainer.get().containerId)
    }

    register("functionTestApp", Test::class) {
        group = "test"
        dependsOn(startDockerContainer)
        finalizedBy(stopDockerContainer)
    }


    compileKotlin2Js {
        kotlinOptions {
            outputFile = "${sourceSets.main.get().output.resourcesDir}/output.js"
            sourceMap = true
            // moduleKind = "plain"
        }
    }

    fun copyJar(outputDir: File, pattern: String) {
        val jar = configurations.compileClasspath.get().single {
            it.name.matches(Regex(pattern))
        }
        copy {
            includeEmptyDirs = false
            from(zipTree(jar))
            into(outputDir)
            include("**/*.js")
            exclude("META-INF/**")
        }
    }

    val jsLibs = listOf(
        "kotlin-stdlib-js-.+\\.jar",
        "kotlinx-serialization-runtime-js-.+\\.jar",
        "kotlinx-coroutines-core-js-.+\\.jar",
        "kunafa-.+\\.jar"

        // waiting for korio to work with kotlin 1.3.3x
//        "klock-js-.+\\.jar",
//        "kmem-js-.+\\.jar",
//        "kds-js-.+\\.jar",
//        "korinject-js-.+\\.jar",
//        "kzlib-js-.+\\.jar",
//        "korio-js-.+\\.jar"
    )

    val unpackJsLibs by registering {
        group = "build"
        description = "Unpack JavaScript standard libraries"
        val outputDir = file("$buildDir/$name")
        inputs.property("compileClasspath", configurations.compileClasspath.get())
        outputs.dir(outputDir)
        doLast {
            jsLibs.forEach { lib ->
                copyJar(outputDir, lib)
            }
        }
    }

    val assembleWeb by registering(Copy::class) {
        group = "build"
        description = "Assemble the web application"
        includeEmptyDirs = false
        from(unpackJsLibs)
        from(sourceSets.main.get().output) {
            exclude("**/*.kjsm")
        }
        into("$buildDir/web")
    }

    assemble {
        dependsOn(assembleWeb)
    }
}