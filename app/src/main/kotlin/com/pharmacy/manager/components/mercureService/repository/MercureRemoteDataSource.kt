package com.pharmacy.manager.components.mercureService.repository

import com.pharmacy.manager.components.chat.model.SendMessageBody
import com.pharmacy.manager.data.rest.RestApi

class MercureRemoteDataSource(private val ra: RestApi) {

    suspend fun sendMessage(chatId: Int, text: String) = ra.sendMessage(chatId, SendMessageBody(text))
        .dataOrThrow()
        .item
}