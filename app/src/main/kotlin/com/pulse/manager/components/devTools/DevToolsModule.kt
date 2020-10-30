package com.pulse.manager.components.devTools

import com.pulse.manager.components.devTools.repository.DevToolsLocalDataSource
import com.pulse.manager.components.devTools.repository.DevToolsRemoteDataSource
import com.pulse.manager.components.devTools.repository.DevToolsRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val devToolsModule = module {

    single { DevToolsRepository(get(), get()) }
    single { DevToolsLocalDataSource(get()) }
    single { DevToolsRemoteDataSource(get()) }

    viewModel { DevToolsViewModel(get()) }

    fragment { DevToolsFragment(get()) }
}