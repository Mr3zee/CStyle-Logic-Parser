val kotlinVersion = "1.5.10"

plugins {
    kotlin("jvm") version "1.5.10"
}

group = "mt.lab.sysoev"
version = "1.0"

val junitVersion = "5.8.2"

repositories {
    mavenCentral()
}

tasks.test.configure {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation(kotlin("reflect"))
}