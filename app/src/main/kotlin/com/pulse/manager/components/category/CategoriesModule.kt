package com.pulse.manager.components.category

import com.pulse.manager.components.category.repository.CategoriesRemoteDataSource
import com.pulse.manager.components.category.repository.CategoriesRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val categoriesModule = module {

    single { CategoriesRepository(get()) }
    single { CategoriesRemoteDataSource(get()) }

    fragment { CategoriesFragment(get()) }

    viewModel { CategoriesViewModel(get()) }
}