plugins {
    kotlin("js") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

group = "ml.xtex"
version = if(System.getenv("GH_REF")?.startsWith("refs/tags/") != true) {
    "dev"
} else {
    System.getenv("GH_REF").substring(10)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.inmo:tgbotapi:5.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("com.soywiz.korlibs.korio:korio:3.2.0")
    implementation("io.github.microutils:kotlin-logging:3.0.2")
}

kotlin {
    js(IR) {
        binaries.executable()
        nodejs {
            runTask {
                workingDir = projectDir
            }
        }
    }
}