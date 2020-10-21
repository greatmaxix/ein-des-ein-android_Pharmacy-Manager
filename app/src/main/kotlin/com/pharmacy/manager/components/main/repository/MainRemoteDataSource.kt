package com.pharmacy.manager.components.main.repository

import com.pharmacy.manager.data.rest.RestApi

class MainRemoteDataSource(private val ra: RestApi) {

    suspend fun getChat(chatId: Int) = ra.getChat(chatId)
        .dataOrThrow()
        .item
}