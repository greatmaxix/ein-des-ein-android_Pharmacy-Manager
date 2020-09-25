package com.pharmacy.manager.components.signIn

import com.pharmacy.manager.components.signIn.repository.SignInLocalDataSource
import com.pharmacy.manager.components.signIn.repository.SignInRemoteDataSource
import com.pharmacy.manager.components.signIn.repository.SignInRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signInModule = module {

    single { SignInRepository(get(), get()) }
    single { SignInLocalDataSource(get()) }
    single { SignInRemoteDataSource(get()) }

    viewModel { SignInViewModel(get()) }

    fragment { SignInFragment(get()) }
}