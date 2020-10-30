package com.pharmacy.manager

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.work.WorkManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.pharmacy.manager.components.aboutApp.aboutAppModule
import com.pharmacy.manager.components.category.categoriesModule
import com.pharmacy.manager.components.chat.chatModule
import com.pharmacy.manager.components.chatList.chatListModule
import com.pharmacy.manager.components.devTools.devToolsModule
import com.pharmacy.manager.components.home.homeModule
import com.pharmacy.manager.components.main.mainModule
import com.pharmacy.manager.components.mercureService.mercureModule
import com.pharmacy.manager.components.needHelp.needHelpModule
import com.pharmacy.manager.components.notifications.notificationsModule
import com.pharmacy.manager.components.product.productCardModule
import com.pharmacy.manager.components.profile.profileModule
import com.pharmacy.manager.components.scanner.codeScannerModule
import com.pharmacy.manager.components.search.searchModule
import com.pharmacy.manager.components.signIn.signInModule
import com.pharmacy.manager.components.splash.splashModule
import com.pharmacy.manager.components.textInfo.textInfoModule
import com.pharmacy.manager.data.local.DBManager
import com.pharmacy.manager.data.local.SPManager
import com.pharmacy.manager.data.rest.restModule
import com.pharmacy.manager.util.CrashlyticsCrashReportingTree
import com.pharmacy.manager.util.HyperlinkedDebugTree
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber
import java.util.*

class App : Application() {

    @KoinExperimentalAPI
    @ExperimentalPagingApi
    @FlowPreview
    override fun onCreate() {
        super.onCreate()

        initCountryCodeMapping()
        initLogger()
        initCrashlytics()
        initKoin()
    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initLogger() {
        Timber.plant(
            if (BuildConfig.DEBUG) HyperlinkedDebugTree() else CrashlyticsCrashReportingTree()
        )
    }

    @KoinExperimentalAPI
    @ExperimentalPagingApi
    @FlowPreview
    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            fragmentFactory()
            modules(
                mainModule,
                restModule,
                managersModule,
                devToolsModule,
                splashModule,
                signInModule,
                homeModule,
                chatListModule,
                chatModule,
                profileModule,
                notificationsModule,
                aboutAppModule,
                needHelpModule,
                textInfoModule,
                codeScannerModule,
                searchModule,
                productCardModule,
                categoriesModule,
                mercureModule
            )
        }
    }

    private val managersModule = module {
        single { SPManager(androidApplication()) }
        single { DBManager(androidApplication()) }
        single { WorkManager.getInstance(androidApplication()) }
    }

    //TODO Find better solution to get country name from ISO3
    companion object {

        var localeMap: MutableMap<String, Locale> = hashMapOf()
            private set

        private fun initCountryCodeMapping() {
            val countries = Locale.getISOCountries()
            localeMap = HashMap(countries.size)
            for (country in countries) {
                val locale = Locale("", country)
                (localeMap as HashMap<String, Locale>)[locale.isO3Country.toUpperCase()] = locale
            }
        }
    }
}