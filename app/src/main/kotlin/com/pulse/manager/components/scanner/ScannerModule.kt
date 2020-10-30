package com.pulse.manager.components.scanner

import com.pulse.manager.components.scanner.repository.ScannerLocalDataSource
import com.pulse.manager.components.scanner.repository.ScannerRemoteDataSource
import com.pulse.manager.components.scanner.repository.ScannerRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val codeScannerModule = module {

    single { ScannerRemoteDataSource(get()) }
    single { ScannerLocalDataSource(get()) }
    single { ScannerRepository(get(), get()) }

    viewModel { ScannerViewModel(get()) }

    fragment { ScannerFragment(get()) }
    fragment { ScannerListFragment(get()) }
}