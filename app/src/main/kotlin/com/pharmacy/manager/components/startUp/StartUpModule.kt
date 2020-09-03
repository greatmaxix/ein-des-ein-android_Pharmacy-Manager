package com.pharmacy.manager.components.startUp

import com.pharmacy.manager.components.startUp.repository.StartUpLocalDataSource
import com.pharmacy.manager.components.startUp.repository.StartUpRemoteDataSource
import com.pharmacy.manager.components.startUp.repository.StartUpRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val startUpModule = module {

    single { StartUpRepository(get(), get()) }
    single { StartUpLocalDataSource(get()) }
    single { StartUpRemoteDataSource(get()) }

    viewModel { StartUpViewModel(get()) }

    fragment { StartUpFragment(get()) }
}