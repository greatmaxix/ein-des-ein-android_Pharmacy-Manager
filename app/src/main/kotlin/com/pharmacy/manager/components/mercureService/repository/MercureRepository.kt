package com.pharmacy.manager.components.mercureService.repository

import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import java.time.LocalDateTime

class MercureRepository(
    private val rds: MercureRemoteDataSource,
    private val lds: MercureLocalDataSource
) {

    suspend fun getUserUuid() = lds.getUserUuid()

    val isChatForeground: Boolean
        get() = lds.isChatForeground

    suspend fun getTopicName() = lds.getTopicName()

    suspend fun insertMessageWithKey(it: MessageItem) {
        it.updateMessageType(lds.getUserUuid())
        lds.insertMessageWithKeys(it)
    }

    suspend fun getLastMessage(chatId: Int) = lds.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = lds.isHeaderExist(chatId, createdAt)

    suspend fun insertChatWithKeys(chat: ChatItem) = lds.insertChatWithKeys(chat)

    suspend fun sendMessage(chatId: Int, text: String) = rds.sendMessage(chatId, text)
}