package com.pulse.manager.components.search

import com.pulse.manager.components.search.repository.SearchRemoteDataSource
import com.pulse.manager.components.search.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@OptIn(KoinApiExtension::class)
val searchModule = module {

    single { SearchRemoteDataSource(get()) }
    single { SearchRepository(get()) }

    viewModel { SearchViewModel() }
}