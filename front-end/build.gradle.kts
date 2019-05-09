plugins {
    id("kotlin2js")
    id("kotlinx-serialization")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.2.1")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.2.1")

    compile("org.jetbrains.kotlin:kotlin-serialization:1.3.30")
    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.11.0")

    implementation("com.narbase:kunafa:0.2.0-beta")

    // waiting for korio to support kotlin 1.3.3x
    // implementation("com.soywiz:korio-js:0.20.0")

    testImplementation(kotlin("stdlib-js"))

    // java only! not js
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1")
}

tasks {
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