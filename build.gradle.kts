// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    kotlin("jvm") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}
buildscript{
    dependencies{
        classpath("com.android.tools.build:gradle:8.4.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.24-1.0.20")
    }
}