import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.ktor.plugin.features.*

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.9"
    kotlin("plugin.serialization") version "1.9.22"
    id("org.jetbrains.dokka") version "2.0.0"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.9")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(kotlin("test-junit"))
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}
