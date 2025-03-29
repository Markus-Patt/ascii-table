plugins {
    kotlin("jvm") version "2.1.20"
}

group = "de.markuspatt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    testImplementation(kotlin("test"))

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    val kotestVersion = "5.9.1"
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")

}

tasks.test {
    useJUnitPlatform {
        includeEngines("kotest")
    }
}

kotlin {
    jvmToolchain(21)
}
