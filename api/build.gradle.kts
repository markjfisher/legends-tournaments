import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.allopen")
    id("com.github.johnrengelman.shadow")
    id("io.spring.dependency-management")
}

val micronautBoMVersion: String by project

dependencyManagement {
    imports {
        mavenBom("io.micronaut:micronaut-bom:$micronautBoMVersion")
    }
}

val junitJupiterEngineVersion: String by project
val jacksonModuleKotlinVersion: String by project
val kotlinLoggingVersion: String by project
val logbackClassicVersion: String by project
val micronautJunit: String by project
val assertJVersion: String by project
val mockkVersion: String by project
val logbackEncoderVersion: String by project
val okHttpVerison: String by project

dependencies {
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-discovery-client")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-management")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleKotlinVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("io.micronaut.configuration:micronaut-hibernate-validator")
    implementation("io.micronaut:micronaut-security")

    kapt("io.micronaut.configuration:micronaut-openapi")
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    kapt("io.micronaut:micronaut-security")

    kaptTest("io.micronaut:micronaut-inject-java")

    implementation("ch.qos.logback:logback-classic:$logbackClassicVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

    testImplementation("io.micronaut.test:micronaut-test-junit5:$micronautJunit")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterEngineVersion")
    testImplementation("io.micronaut:micronaut-inject")
    testImplementation("io.micronaut:micronaut-inject-java")
    testImplementation("io.micronaut:micronaut-http-server-netty")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterEngineVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.squareup.okhttp3:okhttp:$okHttpVerison")
    
}

tasks {
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
    }

    named<JavaExec>("run") {
        jvmArgs(listOf("-noverify", "-XX:TieredStopAtLevel=1"))
    }

    named<KotlinCompile>("compileKotlin") {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }

    named<KotlinCompile>("compileTestKotlin") {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }

    withType<Test> {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    }

    named<Test>("test") {
        useJUnitPlatform()
    }

}

application {
    mainClassName = "tournament.api.Application"
}

allOpen {
    annotation("io.micronaut.aop.Around")
}
