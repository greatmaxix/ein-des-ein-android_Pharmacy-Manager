package com.pulse.manager.components.home

import com.pulse.manager.components.home.repository.HomeLocalDataSource
import com.pulse.manager.components.home.repository.HomeRemoteDataSource
import com.pulse.manager.components.home.repository.HomeRepository
import com.pulse.manager.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val homeModule = module {

    single { HomeRepository(get(), get()) }
    single { HomeLocalDataSource(get<DBManager>().chatItemDAO) }
    single { HomeRemoteDataSource(get()) }

    viewModel { HomeViewModel(get(), get()) }
}