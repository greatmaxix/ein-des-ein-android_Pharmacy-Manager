package com.pulse.manager.components.home.repository

import com.pulse.manager.data.rest.RestApi

class HomeRemoteDataSource(private val ra: RestApi) {

    suspend fun lastOpenedChats() = ra.lastOpenedChats()
        .dataOrThrow()

    suspend fun lastRecommendedProducts() = ra.lastRecommendedProducts()
        .dataOrThrow()
        .items
}