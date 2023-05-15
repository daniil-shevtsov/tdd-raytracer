import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.compose") version "1.4.0"
    id ("org.jetbrains.kotlinx.benchmark") version "0.4.7"
    kotlin("plugin.allopen") version "1.7.22"
    application
    id("info.solidsoft.pitest") version "1.4.7"
}

group = "org.example"
version = "1.0-SNAPSHOT"
sourceSets {
//    val benchmark by creating {
//        // The kotlin plugin will by default recognise Kotlin sources in src/tlib/kotlin
//        compileClasspath += sourceSets["benchmark"].output
//        runtimeClasspath += sourceSets["benchmark"].output
//    }
//    add(benchmark)
}

sourceSets.all {
    java.setSrcDirs(listOf("$name/src"))
    resources.setSrcDirs(listOf("$name/resources"))
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime-jvm:0.4.6")

    testImplementation(kotlin("test"))
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

benchmark {
    targets {
        register("main") {
            this as kotlinx.benchmark.gradle.JvmBenchmarkTarget
            jmhVersion = "1.33" // available only for JVM compilations & Java source sets
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}