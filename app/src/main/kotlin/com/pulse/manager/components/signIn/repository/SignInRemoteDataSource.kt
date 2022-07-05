package com.pulse.manager.components.signIn.repository

import com.pulse.manager.data.rest.RestApi
import com.pulse.manager.data.rest.request.LogInRequest

class SignInRemoteDataSource(private val ra: RestApi) {

    suspend fun login(email: String, password: String) = ra.login(LogInRequest(email, password))
}