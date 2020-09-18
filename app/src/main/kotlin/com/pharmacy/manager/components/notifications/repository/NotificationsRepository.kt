package com.pharmacy.manager.components.notifications.repository

class NotificationsRepository(
    private val rds: NotificationsRemoteDataSource,
    private val lds: NotificationsLocalDataSource
)