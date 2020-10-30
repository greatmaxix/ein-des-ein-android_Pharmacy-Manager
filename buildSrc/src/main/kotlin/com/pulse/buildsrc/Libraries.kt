import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.exclude

object Libraries {

    // internal
    private const val implementation = "implementation"
    private const val kapt = "kapt"

    // kotlin libs
    const val kotlinStdLib = "stdlib"
    const val kotlinReflect = "reflect"

    // lifecycle
    private const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}"
    private const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    private const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLiveData}"
    private const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycleCommon}"
    private const val lifecycleViewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleCommon}"

    fun DependencyHandler.implementLifecycle() {
        add(implementation, lifecycleRuntime)
        add(implementation, lifecycleExtensions)
        add(implementation, lifecycleLivedata)
        add(implementation, lifecycleCommon)
        add(implementation, lifecycleViewmodel)
    }

    // coroutines
    private const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    private const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"

    fun DependencyHandler.implementCoroutines() {
        add(implementation, coroutinesCore)
        add(implementation, coroutinesAndroid)
    }

    // koin di
    private const val koinFragment = "org.koin:koin-androidx-fragment:${Versions.koin}"
    private const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    private const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    private const val koinExt = "org.koin:koin-androidx-ext:${Versions.koin}"

    fun DependencyHandler.implementKoinDI() {
        add(implementation, koinFragment)
        add(implementation, koinViewModel)
        add(implementation, koinScope)
        add(implementation, koinExt)
    }

    // networking
    private const val gson = "com.google.code.gson:gson:${Versions.gson}"
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    private const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.converterGson}"
    private const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    private const val loggingInterceptor = "com.github.ihsanbal:LoggingInterceptor:${Versions.loggingInterceptor}"
    private const val oksse = "com.github.heremaps:oksse:${Versions.oksse}"

    fun DependencyHandler.implementNetworking() {
        add(implementation, gson)
        add(implementation, retrofit)
        add(implementation, converterGson)
        add(implementation, okhttp)
        add(implementation, loggingInterceptor) {
            exclude("org.json", "json")
        }
        add(implementation, oksse)
    }

    // database
    private const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    private const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    private const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    fun DependencyHandler.implementDatabase() {
        add(implementation, roomRuntime)
        add(implementation, roomKtx)
        add(kapt, roomCompiler)
    }

    // androidUi
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
    private const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    private const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationFragment}"
    private const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigationUi}"
    private const val androidMaterial = "com.google.android.material:material:${Versions.androidMaterial}"
    private const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"
    private const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"

    fun DependencyHandler.implementAndroidUI() {
        add(implementation, appcompat)
        add(implementation, coreKtx)
        add(implementation, activity)
        add(implementation, fragment)
        add(implementation, navigationFragment)
        add(implementation, androidMaterial)
        add(implementation, constraintLayout)
        add(implementation, navigationUi)
        add(implementation, viewPager2)
        add(implementation, swipeRefreshLayout)
    }

    // coreUtils
    private const val crashlytics = "com.google.firebase:firebase-crashlytics:${Versions.crashlytics}"
    private const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    private const val kpermissions = "com.github.fondesa:kpermissions:${Versions.kPermissions}"
    private const val cryptoPrefs = "androidx.security:security-crypto:${Versions.cryptoPrefs}"
    private const val firebaseAnalytics = "com.google.firebase:firebase-analytics:${Versions.firebaseAnalytics}"
    private const val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"
    private const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    private const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    private const val workManager = "androidx.work:work-runtime-ktx:${Versions.workManager}"
    private const val androidSdp = "com.intuit.sdp:sdp-android:${Versions.androidScalableDimens}"
    private const val androidSsp = "com.intuit.ssp:ssp-android:${Versions.androidScalableDimens}"
    private const val inputMask = "com.redmadrobot:input-mask-android:${Versions.inputMask}"
    private const val flowBinding = "io.github.reactivecircus.flowbinding:flowbinding-android:${Versions.flowBinding}"
    private const val qrCodeScanner = "com.budiyev.android:code-scanner:${Versions.qrCodeScanner}"
    private const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    private const val notificationsDslCore = "com.kirich1409.android-notification-dsl:core:${Versions.notificationsDsl}"
    private const val notificationsDslExt = "com.kirich1409.android-notification-dsl:extensions:${Versions.notificationsDsl}"

    fun DependencyHandler.implementCoreUtils() {
        add(implementation, crashlytics)
        add(implementation, timber)
        add(implementation, kpermissions)
        add(implementation, cryptoPrefs)
        add(implementation, firebaseAnalytics)
        add(implementation, firebaseMessaging)
        add(implementation, glide)
        add(kapt, glideCompiler)
        add(implementation, workManager)
        add(implementation, androidSdp)
        add(implementation, androidSsp)
        add(implementation, inputMask)
        add(implementation, flowBinding)
        add(implementation, qrCodeScanner)
        add(implementation, paging)
        add(implementation, notificationsDslCore)
        add(implementation, notificationsDslExt)
    }
}