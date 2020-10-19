package com.pharmacy.manager.components.signIn

import com.pharmacy.manager.components.signIn.repository.SignInLocalDataSource
import com.pharmacy.manager.components.signIn.repository.SignInRemoteDataSource
import com.pharmacy.manager.components.signIn.repository.SignInRepository
import com.pharmacy.manager.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val signInModule = module {

    single { SignInRepository(get(), get()) }
    single { SignInLocalDataSource(get(), get<DBManager>().userDAO) }
    single { SignInRemoteDataSource(get()) }

    viewModel { SignInViewModel(get()) }

    fragment { SignInFragment(get()) }
}