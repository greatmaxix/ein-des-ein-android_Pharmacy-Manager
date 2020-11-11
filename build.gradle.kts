import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

apply(from = "${project.rootDir}/script/apk_upload.gradle")

plugins {
    id(BuildPlugins.versionsPlugin) version Versions.versionsPlugin
}

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(BuildPlugins.androidGradlePluginClasspath)
        classpath(
            kotlin(
                BuildPlugins.kotlinGradlePluginClasspath,
                version = Versions.kotlinGradlePlugin
            )
        )
        classpath(BuildPlugins.crashlyticsClasspath)
        classpath(BuildPlugins.appDistributionClasspath)
        classpath(BuildPlugins.safeargsClasspath)
        classpath(BuildPlugins.versionbergClasspath)
        classpath(BuildPlugins.googleServicesClasspath)
        classpath(BuildPlugins.koinClasspath)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<DependencyUpdatesTask> {
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}