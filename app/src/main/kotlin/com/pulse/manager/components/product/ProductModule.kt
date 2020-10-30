package com.pulse.manager.components.product

import com.pulse.manager.components.product.repository.ProductLocalDataSource
import com.pulse.manager.components.product.repository.ProductRemoteDataSource
import com.pulse.manager.components.product.repository.ProductRepository
import com.pulse.manager.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val productCardModule = module {

    single { ProductLocalDataSource(get<DBManager>().recentlyViewedDAO) }
    single { ProductRemoteDataSource(get()) }
    single { ProductRepository(get(), get()) }

    viewModel { ProductViewModel() }

    fragment { ProductFragment(get()) }
}