package com.pharmacy.manager.components.aboutApp

import com.pharmacy.manager.components.aboutApp.repository.AboutAppLocalDataSource
import com.pharmacy.manager.components.aboutApp.repository.AboutAppRemoteDataSource
import com.pharmacy.manager.components.aboutApp.repository.AboutAppRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val aboutAppModule = module {

    single { AboutAppRepository(get(), get()) }
    single { AboutAppLocalDataSource(get()) }
    single { AboutAppRemoteDataSource(get()) }

    viewModel { AboutAppViewModel(get()) }

    fragment { AboutAppFragment(get()) }
}