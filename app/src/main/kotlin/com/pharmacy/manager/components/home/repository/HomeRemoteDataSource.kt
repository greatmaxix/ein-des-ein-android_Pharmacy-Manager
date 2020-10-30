package com.pharmacy.manager.components.home.repository

import com.pharmacy.manager.data.rest.RestApi

class HomeRemoteDataSource(private val ra: RestApi) {

    suspend fun lastOpenedChats() = ra.lastOpenedChats()
        .dataOrThrow()

    suspend fun lastRecommendedProducts() = ra.lastRecommendedProducts()
        .dataOrThrow()
        .items
}