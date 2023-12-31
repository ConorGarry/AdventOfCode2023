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
    testImplementation(kotlin("test"))
}
