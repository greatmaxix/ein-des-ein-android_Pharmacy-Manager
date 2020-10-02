package com.pharmacy.manager.components.chat.repository

import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chat.model.remoteKeys.RemoteKeys

class ChatRepository(
    private val rds: ChatRemoteDataSource,
    private val lds: ChatLocalDataSource
) {

    suspend fun messagesList(chatId: Int, page: Int, pageSize: Int) = rds.messagesList(chatId, page, pageSize)

    suspend fun getUserUuid() = lds.getUserUuid()

    fun getMessagePagingSource(chatId: Int) = lds.getMessagePagingSource(chatId)

    suspend fun clearMessagesCache(chatId: Int) = lds.clearMessages(chatId)

    suspend fun insertMessages(items: List<MessageItem>) = lds.insertMessages(items)

    suspend fun getCount(chatId: Int) = lds.getCount(chatId)

    suspend fun sendMessage(chatId: Int, text: String) = rds.sendMessage(chatId, text)

    suspend fun insertRemoteKeys(items: List<RemoteKeys>) = lds.insertRemoteKeys(items)

    suspend fun getRemoteKeys(messageId: Int) = lds.getRemoteKeys(messageId)

    suspend fun clearRemoteKeys(chatId: Int) = lds.clearRemoteKeys(chatId)
}