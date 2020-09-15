package com.pharmacy.manager.components.startUp.repository

class StartUpRepository(
    private val rds: StartUpRemoteDataSource,
    private val lds: StartUpLocalDataSource
) {

    val isUserLoggedIn
        get() = lds.isUserLoggedIn
}