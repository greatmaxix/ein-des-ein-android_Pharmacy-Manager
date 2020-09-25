package com.pharmacy.manager.components.textInfo

import com.pharmacy.manager.components.textInfo.repository.TextInfoLocalDataSource
import com.pharmacy.manager.components.textInfo.repository.TextInfoRemoteDataSource
import com.pharmacy.manager.components.textInfo.repository.TextInfoRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val textInfoModule = module {

    single { TextInfoRepository(get(), get()) }
    single { TextInfoLocalDataSource(get()) }
    single { TextInfoRemoteDataSource(get()) }

    viewModel { (args: TextInfoFragmentArgs) -> TextInfoViewModel(get(), args) }

    fragment { TextInfoFragment() }
}