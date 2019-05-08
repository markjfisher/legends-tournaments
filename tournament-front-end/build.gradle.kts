plugins {
    id("kotlin2js")
    id("kotlinx-serialization")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.2.1")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.2.1")

    compile("org.jetbrains.kotlin:kotlin-serialization:1.3.30")
    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.11.0")

    implementation("com.narbase:kunafa:0.2.0-beta")

    testImplementation(kotlin("stdlib-js"))
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

    val unpackKotlinJsStdlib by registering {
        group = "build"
        description = "Unpack the Kotlin JavaScript standard library"
        val outputDir = file("$buildDir/$name")
        inputs.property("compileClasspath", configurations.compileClasspath.get())
        outputs.dir(outputDir)
        doLast {
            copyJar(outputDir, "kotlin-stdlib-js-.+\\.jar")
        }
    }

    val unpackKunafaLib by registering {
        group = "build"
        description = "Unpack the kunafa library"
        val outputDir = file("$buildDir/$name")
        inputs.property("compileClasspath", configurations.compileClasspath.get())
        outputs.dir(outputDir)
        doLast {
            copyJar(outputDir, "kunafa-.+\\.jar")
        }
    }

    val unpackSerializationRuntimeLib by registering {
        group = "build"
        description = "Unpack the serialization library"
        val outputDir = file("$buildDir/$name")
        inputs.property("compileClasspath", configurations.compileClasspath.get())
        outputs.dir(outputDir)
        doLast {
            copyJar(outputDir, "kotlinx-serialization-runtime-js-.+\\.jar")
        }
    }

    val unpackCoroutinesLib by registering {
        group = "build"
        description = "Unpack the serialization library"
        val outputDir = file("$buildDir/$name")
        inputs.property("compileClasspath", configurations.compileClasspath.get())
        outputs.dir(outputDir)
        doLast {
            copyJar(outputDir, "kotlinx-coroutines-core-js-.+\\.jar")
        }
    }

    val assembleWeb by registering(Copy::class) {
        group = "build"
        description = "Assemble the web application"
        includeEmptyDirs = false
        from(unpackKotlinJsStdlib)
        from(unpackKunafaLib)
        from(unpackSerializationRuntimeLib)
        from(unpackCoroutinesLib)
        from(sourceSets.main.get().output) {
            exclude("**/*.kjsm")
        }
        into("$buildDir/web")
    }

    assemble {
        dependsOn(assembleWeb)
    }
}