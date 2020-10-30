package com.pulse.manager.components.signIn.repository

import com.pulse.manager.data.rest.response.LogInResponse

class SignInRepository(
    private val rds: SignInRemoteDataSource,
    private val lds: SignInLocalDataSource
) {

    suspend fun login(email: String, password: String) = rds.login(email, password)
        .dataOrThrow()
        .apply { saveInitialData(this) }
        .item

    private suspend fun saveInitialData(response: LogInResponse) {
        lds.setAccessToken(response.token)
        lds.setRefreshToken(response.refreshToken)
        lds.saveUser(response.item)
    }
}