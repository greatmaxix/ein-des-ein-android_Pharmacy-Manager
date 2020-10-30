package com.pharmacy.manager.components.profile.repository

import com.pharmacy.manager.data.rest.RestApi
import com.pharmacy.manager.data.rest.request.LogOutRequest

class ProfileRemoteDataSource(private val ra: RestApi) {

    suspend fun logout(refreshToken: String) = ra.logout(LogOutRequest(refreshToken))
}