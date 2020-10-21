package com.pharmacy.manager.components.home

import com.pharmacy.manager.components.home.repository.HomeLocalDataSource
import com.pharmacy.manager.components.home.repository.HomeRemoteDataSource
import com.pharmacy.manager.components.home.repository.HomeRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val homeModule = module {

    single { HomeRepository(get(), get()) }
    single { HomeLocalDataSource(get()) }
    single { HomeRemoteDataSource(get()) }

    viewModel { HomeViewModel(get(), get(), get()) }

    fragment { HomeFragment(get()) }
}