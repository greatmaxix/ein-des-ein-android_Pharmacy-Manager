package com.pulse.manager.components.main.repository

class MainRepository(
    private val lds: MainLocalDataSource,
    private val rds: MainRemoteDataSource
) {

    fun getUserLiveData() = lds.getUserLiveData()

    fun setChatForeground(isForeground: Boolean) = lds.setChatForeground(isForeground)

    suspend fun getChat(chatId: Int) = lds.getChat(chatId) ?: rds.getChat(chatId)
}