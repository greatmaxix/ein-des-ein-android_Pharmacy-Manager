package com.pulse.manager.components.profile.repository

class ProfileRepository(
    private val rds: ProfileRemoteDataSource,
    private val lds: ProfileLocalDataSource
) {

    fun getUser() = lds.getUser()

    suspend fun logout() = rds.logout(lds.refreshToken).apply {
        lds.logout()
    }.dataOrThrow()
}