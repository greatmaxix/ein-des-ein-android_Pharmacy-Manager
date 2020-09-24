package com.pharmacy.manager.components.profile

import com.pharmacy.manager.components.profile.repository.ProfileLocalDataSource
import com.pharmacy.manager.components.profile.repository.ProfileRemoteDataSource
import com.pharmacy.manager.components.profile.repository.ProfileRepository
import com.pharmacy.manager.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {

    single { ProfileRepository(get(), get()) }
    single { ProfileLocalDataSource(get(), get<DBManager>().userDAO) }
    single { ProfileRemoteDataSource(get()) }

    viewModel { ProfileViewModel(get()) }

    fragment { ProfileFragment(get()) }
}