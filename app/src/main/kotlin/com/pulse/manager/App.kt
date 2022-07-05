package com.pulse.manager

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.work.WorkManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.pulse.manager.components.about.aboutAppModule
import com.pulse.manager.components.category.categoriesModule
import com.pulse.manager.components.chat.chatModule
import com.pulse.manager.components.chat_list.chatListModule
import com.pulse.manager.components.home.homeModule
import com.pulse.manager.components.language.languageModule
import com.pulse.manager.components.main.mainModule
import com.pulse.manager.components.mercureService.mercureModule
import com.pulse.manager.components.needHelp.needHelpModule
import com.pulse.manager.components.notifications.notificationsModule
import com.pulse.manager.components.product.productCardModule
import com.pulse.manager.components.profile.profileModule
import com.pulse.manager.components.scanner.codeScannerModule
import com.pulse.manager.components.search.searchModule
import com.pulse.manager.components.signIn.signInModule
import com.pulse.manager.components.splash.splashModule
import com.pulse.manager.components.statistic.statisticModule
import com.pulse.manager.components.textInfo.textInfoModule
import com.pulse.manager.core.extensions.dataStore
import com.pulse.manager.core.locale.LocaleManager
import com.pulse.manager.data.local.DBManager
import com.pulse.manager.data.rest.restModule
import com.pulse.manager.util.CrashlyticsCrashReportingTree
import com.pulse.manager.util.HyperlinkedDebugTree
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
@KoinExperimentalAPI
@ExperimentalPagingApi
@FlowPreview
class App : Application() {

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

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            fragmentFactory()
            modules(
                mainModule,
                restModule,
                managersModule,
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
                mercureModule,
                statisticModule,
                languageModule
            )
        }
    }

    private val managersModule = module {
        single { androidContext().dataStore }
        single { DBManager(androidApplication()) }
        single { WorkManager.getInstance(androidApplication()) }
        single { LocaleManager.getInstance(get()) }
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