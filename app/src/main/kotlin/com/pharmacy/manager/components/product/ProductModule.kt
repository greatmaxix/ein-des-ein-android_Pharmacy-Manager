package com.pharmacy.manager.components.product

import com.pharmacy.manager.components.product.repository.ProductRemoteDataSource
import com.pharmacy.manager.components.product.repository.ProductRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productCardModule = module {

    single { ProductRemoteDataSource(get()) }
    single { ProductRepository(get()) }

    viewModel { ProductViewModel() }

    fragment { ProductFragment(get()) }
}