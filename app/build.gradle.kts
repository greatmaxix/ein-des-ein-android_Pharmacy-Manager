import Libraries.implementAndroidUI
import Libraries.implementCoreUtils
import Libraries.implementCoroutines
import Libraries.implementDatabase
import Libraries.implementKoinDI
import Libraries.implementLifecycle
import Libraries.implementNetworking
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.pulse.buildsrc.SigningConfigs
import com.pulse.buildsrc.task.GenerateNavArgsProguardRulesTask
import com.pulse.buildsrc.task.NAVARGS_PROGUARD_RULES_PATH

plugins {
    id(BuildPlugins.androidApplicationPlugin)
    kotlin(BuildPlugins.kotlinAndroidPlugin)
    kotlin(BuildPlugins.kotlinAndroidExtensionsPlugin)
    kotlin(BuildPlugins.kaptPlugin)
//    kotlin(BuildPlugins.parcelizePlugin) // TODO uncomment in future
    id(BuildPlugins.safeargsPlugin)
    id(BuildPlugins.googleServicesPlugin)
    id(BuildPlugins.crashlyticsPlugin)
    id(BuildPlugins.appDistributionPlugin)
}

apply(from = "${project.rootDir}/script/experimentalExtensions.gradle")

tasks {
    named("preBuild").dependsOn(register("generateNavArgsProguardRules", GenerateNavArgsProguardRulesTask::class))
}

android {
    compileSdkVersion(DefaultConfig.compileSdk)

    defaultConfig {
        applicationId = "com.pulse.manager"

        consumerProguardFile(File(buildDir, NAVARGS_PROGUARD_RULES_PATH))

        with(DefaultConfig) {
            val buildCode = "git rev-list --count remotes/origin/master remotes/origin/develop".execute.toInt()
            minSdkVersion(minSdk)
            targetSdkVersion(targetSdk)
            versionCode(buildCode)
            versionName(versionName + buildCode)
        }

        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = "$applicationId-v.$versionName($versionCode)-$name.apk"
            }
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", true)
                arg("room.expandProjection", true)
            }
        }
    }

    val release = "release"

    signingConfigs {
        create(release) {
            with(SigningConfigs) {
                keyAlias = alias
                keyPassword = password_key
                storePassword = password_store
                storeFile = rootProject.file(keystore)
            }
        }
    }

    buildTypes {
        getByName(release) {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(release)
        }
        create("qa") {
            isMinifyEnabled = true
            isShrinkResources = true
            // isDebuggable = true
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-qa"
            firebaseAppDistribution {
                releaseNotes = "git log --pretty=format:${"%s"} -20 --merges".execute
                    .split("\n")
                    .filter { it.contains("fix/") || it.contains("feature/") || it.contains("hotfix/") }
                    .joinToString("\n") {
                        it.replace("(.*/+)", "")
                            .replace("release(.)*\n", "")
                            .replace("\"", "")
                            .replace("('.*')", "")
                    }
                testers = "developereinios@gmail.com, ivan.kovalenko13@gmail.com"
            }
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-dg"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isAbortOnError = false
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = mutableListOf<String>().apply {
            addAll(freeCompilerArgs)
            addAll(listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xopt-in=kotlin.OptIn"))
        }
    }
}

dependencies {
    // libs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar", "*.jar"))))

    // kotlin
    implementation(kotlin(Libraries.kotlinStdLib))
    implementation(kotlin(Libraries.kotlinReflect))

    // main dependencies
    implementLifecycle()
    implementCoroutines()
    implementKoinDI()
    implementNetworking()
    implementDatabase()
    implementAndroidUI()
    implementCoreUtils()
}

val String.execute
    get() = org.apache.commons.io.output.ByteArrayOutputStream().run {
        project.exec {
            commandLine = split(" ")
            standardOutput = this@run
        }
        String(toByteArray()).trim()
    }