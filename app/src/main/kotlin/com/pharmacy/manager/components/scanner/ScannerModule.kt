package com.pharmacy.manager.components.scanner

import com.pharmacy.manager.components.scanner.repository.ScannerLocalDataSource
import com.pharmacy.manager.components.scanner.repository.ScannerRemoteDataSource
import com.pharmacy.manager.components.scanner.repository.ScannerRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val codeScannerModule = module {

    single { ScannerRemoteDataSource(get()) }
    single { ScannerLocalDataSource(get()) }
    single { ScannerRepository(get(), get()) }

    viewModel { ScannerViewModel(get()) }

    fragment { ScannerFragment(get()) }
    fragment { ScannerListFragment(get()) }
}