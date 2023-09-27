// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.android.library") version "7.4.2" apply false
    kotlin("jvm") version "1.8.0" apply false
}
buildscript {
    dependencies{
        classpath(kotlin("gradle-plugin", version = "1.8.0"))
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}
extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}