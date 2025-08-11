import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.9"
    kotlin("plugin.serialization") version "1.9.22"
}

group = "com.hereliesaz.reverselookupapi"
version = "0.0.1"

application {
    mainClass.set("com.hereliesaz.reverselookupapi.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.seleniumhq.selenium:selenium-java:4.15.0")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(kotlin("test-junit"))
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
    docker {
        // jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_11)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}
