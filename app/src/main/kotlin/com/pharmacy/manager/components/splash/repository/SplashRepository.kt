package com.pharmacy.manager.components.splash.repository

class SplashRepository(
    private val rds: SplashRemoteDataSource,
    private val lds: SplashLocalDataSource
) {

    val isUserLoggedIn
        get() = lds.isUserLoggedIn

    suspend fun fetchUser() = rds.fetchUser()
        .dataOrThrow()
        .apply { lds.saveUser(item) }
}