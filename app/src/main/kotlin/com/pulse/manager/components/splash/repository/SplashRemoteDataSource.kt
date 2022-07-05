package com.pulse.manager.components.splash.repository

import com.pulse.manager.data.rest.RestApi

class SplashRemoteDataSource(private val ra: RestApi) {

    suspend fun fetchUser() = ra.fetchUser()
}