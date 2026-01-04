import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.3.8"
    java
}

val minecraftVersion: String by project
val loaderVersion: String by project
val modVersion: String by project
val javaVersion: String by project

group = "com.jarbot"
version = modVersion

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
}

dependencies {
    // Minecraft + mappings + loader + fabric-api
    minecraft("com.mojang:minecraft:$minecraftVersion")

    // Mappings: you may need to change the mapping string to match the exact published yarn for 1.21.10
    mappings("net.fabricmc:yarn:${minecraftVersion}+build.1:v2")

    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")

    // Fabric API: change to a specific published version if necessary for 1.21.10
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.86.2+${minecraftVersion}")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
    }
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}