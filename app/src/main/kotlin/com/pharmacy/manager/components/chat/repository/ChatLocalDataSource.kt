package com.pharmacy.manager.components.chat.repository

import com.pharmacy.manager.components.chat.model.message.MessageDAO
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chat.model.remoteKeys.MessagesRemoteKeys.Companion.createRemoteKey
import com.pharmacy.manager.components.chat.model.remoteKeys.MessagesRemoteKeysDAO
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager
import java.time.LocalDateTime

class ChatLocalDataSource(
    private val sp: SPManager,
    private val userDao: UserDAO,
    private val messagesRemoteKeysDAO: MessagesRemoteKeysDAO,
    private val messageDAO: MessageDAO,
) {

    suspend fun getUserUuid() = userDao.getUser()?.uuid

    fun getMessagePagingSource(chatId: Int) = messageDAO.getMessagePagingSource(chatId)

    fun getLastMessageLiveData(chatId: Int) = messageDAO.getLastMessageLiveData(chatId)

    suspend fun getRemoteKeys(messageId: Int) = messagesRemoteKeysDAO.getRemoteKeys(messageId)

    suspend fun clearMessages(chatId: Int) {
        messageDAO.clearChat(chatId)
        messagesRemoteKeysDAO.clearRemoteKeys(chatId)
    }

    suspend fun insertMessagesWithKeys(messages: List<MessageItem>) {
        val keys = messages.map { messageItem -> createRemoteKey(messageItem) }
        messageDAO.insert(messages)
        messagesRemoteKeysDAO.insert(keys)
    }

    suspend fun getLastMessage(chatId: Int) = messageDAO.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = messageDAO.getHeaderMessages(chatId)
        .find { it.createdAt.year == createdAt.year && it.createdAt.month == createdAt.month && it.createdAt.dayOfMonth == createdAt.dayOfMonth } != null
}