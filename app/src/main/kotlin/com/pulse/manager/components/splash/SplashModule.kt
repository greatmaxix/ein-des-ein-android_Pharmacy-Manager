package com.pulse.manager.components.splash

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import com.pulse.manager.components.splash.SplashViewModel.Companion.UPDATE_PROFILE_INFO
import com.pulse.manager.components.splash.repository.SplashLocalDataSource
import com.pulse.manager.components.splash.repository.SplashRemoteDataSource
import com.pulse.manager.components.splash.repository.SplashRepository
import com.pulse.manager.components.splash.worker.UpdateProfileInfoWorker
import com.pulse.manager.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val splashModule = module {

    single { SplashRepository(get(), get()) }
    single { SplashLocalDataSource(get(), get<DBManager>().userDAO) }
    single { SplashRemoteDataSource(get()) }

    viewModel { SplashViewModel(get(), get()) }

    factory(named(UPDATE_PROFILE_INFO)) {
        OneTimeWorkRequestBuilder<UpdateProfileInfoWorker>().setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        ).build()
    }
}