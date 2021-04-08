package com.pulse.manager.components.notifications

import com.pulse.manager.components.notifications.repository.NotificationsLocalDataSource
import com.pulse.manager.components.notifications.repository.NotificationsRemoteDataSource
import com.pulse.manager.components.notifications.repository.NotificationsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val notificationsModule = module {

    single { NotificationsRepository(get(), get()) }
    single { NotificationsLocalDataSource(get()) }
    single { NotificationsRemoteDataSource(get()) }

    viewModel { NotificationsViewModel(get()) }
}