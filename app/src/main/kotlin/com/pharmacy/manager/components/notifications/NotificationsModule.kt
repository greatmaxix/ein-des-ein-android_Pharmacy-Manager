package com.pharmacy.manager.components.notifications

import com.pharmacy.manager.components.notifications.repository.NotificationsLocalDataSource
import com.pharmacy.manager.components.notifications.repository.NotificationsRemoteDataSource
import com.pharmacy.manager.components.notifications.repository.NotificationsRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notificationsModule = module {

    single { NotificationsRepository(get(), get()) }
    single { NotificationsLocalDataSource(get()) }
    single { NotificationsRemoteDataSource(get()) }

    viewModel { NotificationsViewModel(get()) }

    fragment { NotificationsFragment(get()) }
}