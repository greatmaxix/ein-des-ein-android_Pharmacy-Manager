package com.pharmacy.manager.components.needHelp

import com.pharmacy.manager.components.needHelp.repository.NeedHelpLocalDataSource
import com.pharmacy.manager.components.needHelp.repository.NeedHelpRemoteDataSource
import com.pharmacy.manager.components.needHelp.repository.NeedHelpRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val needHelpModule = module {

    single { NeedHelpRepository(get(), get()) }
    single { NeedHelpLocalDataSource(get()) }
    single { NeedHelpRemoteDataSource(get()) }

    viewModel { NeedHelpViewModel(get()) }

    fragment { NeedHelpFragment(get()) }
}