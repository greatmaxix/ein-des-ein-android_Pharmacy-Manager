package com.pharmacy.manager.components.search

import com.pharmacy.manager.components.search.repository.SearchRemoteDataSource
import com.pharmacy.manager.components.search.repository.SearchRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val searchModule = module {

    single { SearchRemoteDataSource(get()) }
    single { SearchRepository(get()) }

    viewModel { SearchViewModel() }

    fragment { SearchFragment(get()) }
}