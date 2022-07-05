package com.pulse.manager.components.about

import com.pulse.manager.components.about.repository.AboutAppLocalDataSource
import com.pulse.manager.components.about.repository.AboutAppRemoteDataSource
import com.pulse.manager.components.about.repository.AboutAppRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val aboutAppModule = module {

    single { AboutAppRepository(get(), get()) }
    single { AboutAppLocalDataSource(get()) }
    single { AboutAppRemoteDataSource(get()) }

    viewModel { AboutAppViewModel(get()) }
}