package com.pharmacy.manager.components.home.repository

class HomeRepository(
    private val rds: HomeRemoteDataSource,
    private val lds: HomeLocalDataSource
) {

    suspend fun lastOpenedChats() = rds.lastOpenedChats()

    suspend fun lastRecommendedProducts() = rds.lastRecommendedProducts()

    fun getOpenedChatsLiveData() = lds.getOpenedChatsLiveData()
}