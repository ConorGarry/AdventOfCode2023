plugins {
    kotlin("jvm") version "1.9.21"
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
    test {
        useJUnit()
    }
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    testImplementation(kotlin("test"))
}
