package com.pharmacy.manager.components.textInfo

import com.pharmacy.manager.components.textInfo.repository.TextInfoLocalDataSource
import com.pharmacy.manager.components.textInfo.repository.TextInfoRemoteDataSource
import com.pharmacy.manager.components.textInfo.repository.TextInfoRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val textInfoModule = module {

    single { TextInfoRepository(get(), get()) }
    single { TextInfoLocalDataSource(get()) }
    single { TextInfoRemoteDataSource(get()) }

    viewModel { (args: TextInfoFragmentArgs) -> TextInfoViewModel(get(), args) }

    fragment { TextInfoFragment() }
}