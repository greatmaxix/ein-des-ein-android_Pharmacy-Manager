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
import io.github.rockerhieu.versionberg.Git.getCommitCount
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(from = "${project.rootDir}/script/experimentalExtensions.gradle")

plugins {
    id(BuildPlugins.androidApplicationPlugin)
    kotlin(BuildPlugins.kotlinAndroidPlugin)
    kotlin(BuildPlugins.kotlinAndroidExtensionsPlugin)
    kotlin(BuildPlugins.kaptPlugin)
//    kotlin(BuildPlugins.parcelizePlugin) // TODO uncomment in future
    id(BuildPlugins.versionbergPlugin)
    id(BuildPlugins.safeargsPlugin)
    id(BuildPlugins.googleServicesPlugin)
    id(BuildPlugins.crashlyticsPlugin)
    id(BuildPlugins.appDistributionPlugin)
}

with(tasks) {
    withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlin.OptIn"
        )
    }
    named("preBuild").dependsOn(
        register(
            "generateNavArgsProguardRules",
            GenerateNavArgsProguardRulesTask::class
        )
    )
}

versionberg {
    setMajor(DefaultConfig.versionMajor)
    setMinor(DefaultConfig.versionMinor)
    nameTemplate = "$major.$minor.${getCommitCount(gitDir)}}"
    codeTemplate = "((($major * 100) + $minor) * 100) * 100000 + $build"
}

android {
    compileSdkVersion(DefaultConfig.compileSdk)

    defaultConfig {
        applicationId = DefaultConfig.applicationId

        consumerProguardFile(File(buildDir, NAVARGS_PROGUARD_RULES_PATH))

        with(DefaultConfig) {
            minSdkVersion(minSdk)
            targetSdkVersion(targetSdk)
        }
        versionCode = versionberg.code
        versionName = versionberg.name

        proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName =
                    "../../apk/$applicationId-$name-$versionName($versionCode).apk"
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

    val debug = "debug"
    val qa = "qa"
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
        create(qa) {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-$qa"
            firebaseAppDistribution {
                releaseNotes = "Some text"
                testers = "developereinios@gmail.com, ivan.kovalenko13@gmail.com"
            }
        }
        getByName(debug) {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName(release)
            versionNameSuffix = "-$debug"
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