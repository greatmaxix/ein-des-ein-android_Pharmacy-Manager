package com.pulse.manager.components.main.repository

import com.pulse.manager.data.rest.RestApi

class MainRemoteDataSource(private val ra: RestApi) {

    suspend fun getChat(chatId: Int) = ra.getChat(chatId)
        .dataOrThrow()
        .item
}