plugins {
    kotlin("js") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

group = "ml.xtex"
if(System.getenv("GH_REF")?.startsWith("refs/tags/") != true) {
    version = "dev"
} else {
    version = System.getenv("GH_REF").substring(10)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.inmo:tgbotapi:3.2.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("com.squareup.okio:okio-nodefilesystem:3.2.0")
    implementation("io.github.microutils:kotlin-logging:3.0.0")
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