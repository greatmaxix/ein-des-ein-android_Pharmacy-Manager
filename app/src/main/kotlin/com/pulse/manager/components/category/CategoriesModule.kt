package com.pulse.manager.components.category

import com.pulse.manager.components.category.model.Category
import com.pulse.manager.components.category.repository.CategoriesLocalDataSource
import com.pulse.manager.components.category.repository.CategoriesRemoteDataSource
import com.pulse.manager.components.category.repository.CategoriesRepository
import com.pulse.manager.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val categoriesModule = module {

    single { CategoriesRepository(get(), get()) }
    single { CategoriesRemoteDataSource(get()) }
    single { CategoriesLocalDataSource(get<DBManager>().categoryDAO) }

    viewModel { (category: Category?) -> CategoriesViewModel(get(), category) }
}