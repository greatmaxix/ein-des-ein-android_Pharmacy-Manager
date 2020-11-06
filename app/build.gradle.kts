import Libraries.implementAndroidUI
import Libraries.implementCoreUtils
import Libraries.implementCoroutines
import Libraries.implementDatabase
import Libraries.implementKoinDI
import Libraries.implementLifecycle
import Libraries.implementNetworking
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import io.github.rockerhieu.versionberg.Git.getCommitCount
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(BuildPlugins.androidApplicationPlugin)
    kotlin(BuildPlugins.kotlinAndroidPlugin)
    kotlin(BuildPlugins.kotlinAndroidExtensionsPlugin)
    kotlin(BuildPlugins.kaptPlugin)
    id(BuildPlugins.versionbergPlugin)
    id(BuildPlugins.safeargsPlugin)
    id(BuildPlugins.googleServicesPlugin)
    id(BuildPlugins.crashlyticsPlugin)
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xopt-in=kotlin.OptIn"
    )
}

versionberg {
    setMajor(App.major)
    setMinor(App.minor)
    nameTemplate = "$major.$minor.${getCommitCount(gitDir)}}"
    codeTemplate = "((($major * 100) + $minor) * 100) * 100000 + $build"
}

android {
    compileSdkVersion(AndroidSdk.compile)
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        applicationId = App.applicationId
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = versionberg.code
        versionName = versionberg.name

        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = "../../apk/$applicationId-$name-$versionName($versionCode).apk"
            }
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
                arg("room.expandProjection", "true")
            }
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../keystore/debug.p12")
            storePassword = "default_1Android_2Debug_3Key"
            keyAlias = "defaultDebug"
            keyPassword = "default_1Android_2Debug_3Key"
        }
        create("release") {
            storeFile = file("../keystore/debug.p12") // TODO change to release key
            storePassword = "default_1Android_2Debug_3Key"
            keyAlias = "defaultDebug"
            keyPassword = "default_1Android_2Debug_3Key"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            isMinifyEnabled = false
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")

            versionNameSuffix = "-dg"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")
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