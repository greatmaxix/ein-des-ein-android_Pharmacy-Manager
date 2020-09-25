package com.pharmacy.manager.components.splash

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import com.pharmacy.manager.components.splash.SplashViewModel.Companion.UPDATE_PROFILE_INFO
import com.pharmacy.manager.components.splash.repository.SplashLocalDataSource
import com.pharmacy.manager.components.splash.repository.SplashRemoteDataSource
import com.pharmacy.manager.components.splash.repository.SplashRepository
import com.pharmacy.manager.components.splash.worker.UpdateProfileInfoWorker
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val splashModule = module {

    single { SplashRepository(get(), get()) }
    single { SplashLocalDataSource(get()) }
    single { SplashRemoteDataSource(get()) }

    viewModel { SplashViewModel(get(), get()) }

    fragment { SplashFragment(get()) }

    factory(named(UPDATE_PROFILE_INFO)) {
        OneTimeWorkRequestBuilder<UpdateProfileInfoWorker>().setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        ).build()
    }
}