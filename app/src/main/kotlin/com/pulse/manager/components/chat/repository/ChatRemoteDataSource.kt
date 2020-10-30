package com.pulse.manager.components.chat.repository

import com.pulse.manager.components.chat.model.SendMessageBody
import com.pulse.manager.data.rest.RestApi
import okhttp3.MultipartBody

class ChatRemoteDataSource(private val ra: RestApi) {

    suspend fun messagesList(chatId: Int, pageSize: Int?, afterMessageNumber: Int?, beforeMessageNumber: Int?) =
        ra.messagesList(chatId, pageSize, afterMessageNumber, beforeMessageNumber)
            .dataOrThrow()

    suspend fun sendMessage(chatId: Int, text: String) = ra.sendMessage(chatId, SendMessageBody(text))
        .dataOrThrow()
        .item

    suspend fun sendProductMessage(chatId: Int, globalProductId: Int) = ra.sendProductMessage(chatId, globalProductId)
        .dataOrThrow()
        .item

    suspend fun sendImageMessage(chatId: Int, fileUuid: String) = ra.sendImageMessage(chatId, fileUuid)
        .dataOrThrow()
        .item

    suspend fun uploadImage(partBody: MultipartBody.Part) = ra.uploadImage(partBody)
        .dataOrThrow()
        .item

    suspend fun requestCloseChat(chatId: Int) = ra.closeChat(chatId)
        .dataOrThrow()
        .item
}