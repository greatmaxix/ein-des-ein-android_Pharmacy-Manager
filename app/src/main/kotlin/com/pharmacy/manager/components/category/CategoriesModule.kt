package com.pharmacy.manager.components.category

import com.pharmacy.manager.components.category.repository.CategoriesRemoteDataSource
import com.pharmacy.manager.components.category.repository.CategoriesRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoriesModule = module {

    single { CategoriesRepository(get()) }
    single { CategoriesRemoteDataSource(get()) }

    fragment { CategoriesFragment(get()) }

    viewModel { CategoriesViewModel(get()) }
}