object BuildPlugins {

    const val versionsPlugin = "com.github.ben-manes.versions"

    const val androidGradlePluginClasspath = "com.android.tools.build:gradle:${Versions.gradleBuildTools}"
    const val kotlinGradlePluginClasspath = "gradle-plugin"
    const val crashlyticsClasspath = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlyticsPlugin}"
    const val appDistributionClasspath = "com.google.firebase:firebase-appdistribution-gradle:${Versions.appDistributionPlugin}"
    const val safeargsClasspath = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationSafeArgsGradle}"
    const val googleServicesClasspath = "com.google.gms:google-services:${Versions.googleServices}"
    const val koinClasspath = "org.koin:koin-gradle-plugin:${Versions.koin}"

    const val androidApplicationPlugin = "com.android.application"
    const val kotlinAndroidPlugin = "android"
    const val kotlinAndroidExtensionsPlugin = "android.extensions"
    const val kaptPlugin = "kapt"
    const val parcelizePlugin = "parcelize"
    const val safeargsPlugin = "androidx.navigation.safeargs.kotlin"
    const val googleServicesPlugin = "com.google.gms.google-services"
    const val crashlyticsPlugin = "com.google.firebase.crashlytics"
    const val appDistributionPlugin = "com.google.firebase.appdistribution"
}