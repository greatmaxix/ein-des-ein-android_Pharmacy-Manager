package com.pulse.manager.components.main

import com.pulse.manager.components.main.repository.MainLocalDataSource
import com.pulse.manager.components.main.repository.MainRemoteDataSource
import com.pulse.manager.components.main.repository.MainRepository
import com.pulse.manager.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val mainModule = module {

    single { MainRemoteDataSource(get()) }
    single { MainLocalDataSource(get(), get<DBManager>().userDAO, get<DBManager>().chatItemDAO) }
    single { MainRepository(get(), get()) }

    viewModel { MainViewModel(get()) }
}