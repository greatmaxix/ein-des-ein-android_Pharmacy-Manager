package com.pulse.manager.components.profile.repository

import com.pulse.manager.data.rest.RestApi
import com.pulse.manager.data.rest.request.LogOutRequest

class ProfileRemoteDataSource(private val ra: RestApi) {

    suspend fun logout(refreshToken: String) = ra.logout(LogOutRequest(refreshToken))
}