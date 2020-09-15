package com.pharmacy.manager.components.devTools

import com.pharmacy.manager.components.devTools.repository.DevToolsLocalDataSource
import com.pharmacy.manager.components.devTools.repository.DevToolsRemoteDataSource
import com.pharmacy.manager.components.devTools.repository.DevToolsRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val devToolsModule = module {

    single { DevToolsRepository(get(), get()) }
    single { DevToolsLocalDataSource(get()) }
    single { DevToolsRemoteDataSource(get()) }

    viewModel { DevToolsViewModel(get()) }

    fragment { DevToolsFragment(get()) }
}