package com.pharmacy.manager.components.chat.repository

import com.pharmacy.manager.components.chat.model.SendMessageBody
import com.pharmacy.manager.data.rest.RestApi

class ChatRemoteDataSource(private val ra: RestApi) {

    suspend fun messagesList(chatId: Int, page: Int?, pageSize: Int?) = ra.messagesList(chatId, page, pageSize)

    suspend fun sendMessage(chatId: Int, text: String) = ra.sendMessage(chatId, SendMessageBody(text))
}