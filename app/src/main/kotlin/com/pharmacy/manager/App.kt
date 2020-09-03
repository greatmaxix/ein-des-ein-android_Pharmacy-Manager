package com.pharmacy.manager

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.pharmacy.manager.components.devTools.devToolsModule
import com.pharmacy.manager.components.startUp.startUpModule
import com.pharmacy.manager.data.local.DBManager
import com.pharmacy.manager.data.local.SPManager
import com.pharmacy.manager.data.rest.restModule
import com.pharmacy.manager.util.CrashlyticsCrashReportingTree
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initCrashlytics()
        initKoin()
    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initLogger() {
        Timber.plant(
            if (BuildConfig.DEBUG) Timber.DebugTree() else CrashlyticsCrashReportingTree()
        )
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            fragmentFactory()
            modules(
                restModule,
                managersModule,
                devToolsModule,
                startUpModule
            )
        }
    }

    private val managersModule = module {
        single { SPManager(androidApplication()) }
        single { DBManager(androidApplication()) }
    }
}