
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        // other repositories...
        mavenCentral()
    }
    dependencies {
        // other plugins...
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false



        id("com.google.dagger.hilt.android") version "2.44" apply false
}

