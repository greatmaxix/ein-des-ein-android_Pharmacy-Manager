package com.pulse.manager.components.chat.repository

import com.pulse.manager.components.chat.model.message.MessageItem
import okhttp3.MultipartBody
import java.time.LocalDateTime

class ChatRepository(
    private val rds: ChatRemoteDataSource,
    private val lds: ChatLocalDataSource
) {

    suspend fun getUserUuid() = lds.getUserUuid()

    suspend fun fetchMessagesList(chatId: Int, pageSize: Int?, afterMessageNumber: Int?, beforeMessageNumber: Int?) =
        rds.messagesList(chatId, pageSize, afterMessageNumber, beforeMessageNumber)

    fun getMessagePagingSource(chatId: Int) = lds.getMessagePagingSource(chatId)

    fun getLastMessageFlow(chatId: Int) = lds.getLastMessageFlow(chatId)

    fun getChatFlow(chatId: Int) = lds.getChatFlow(chatId)

    suspend fun clearMessages(chatId: Int) = lds.clearMessages(chatId)

    suspend fun insertMessagesWithKeys(messages: List<MessageItem>) = lds.insertMessagesWithKeys(messages)

    suspend fun getRemoteKeys(messageId: Int) = lds.getRemoteKeys(messageId)

    suspend fun sendMessage(chatId: Int, text: String) = rds.sendMessage(chatId, text)

    suspend fun sendProductMessage(chatId: Int, globalProductId: Int) = rds.sendProductMessage(chatId, globalProductId)

    suspend fun sendImageMessage(chatId: Int, fileUuid: String) = rds.sendImageMessage(chatId, fileUuid)

    suspend fun uploadImage(partBody: MultipartBody.Part) = rds.uploadImage(partBody)

    suspend fun getLastMessage(chatId: Int) = lds.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = lds.isHeaderExist(chatId, createdAt)

    suspend fun requestCloseChat(chatId: Int) = rds.requestCloseChat(chatId)
}