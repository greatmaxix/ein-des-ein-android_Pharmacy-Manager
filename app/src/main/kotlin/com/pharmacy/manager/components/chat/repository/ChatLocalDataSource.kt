package com.pharmacy.manager.components.chat.repository

import com.pharmacy.manager.components.chat.model.message.MessageDAO
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chat.model.remoteKeys.RemoteKeys
import com.pharmacy.manager.components.chat.model.remoteKeys.RemoteKeysDAO
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager

class ChatLocalDataSource(
    private val sp: SPManager,
    private val userDao: UserDAO,
    private val remoteKeysDAO: RemoteKeysDAO,
    private val messageDAO: MessageDAO,
) {

    suspend fun getUserUuid() = userDao.getUser()?.uuid

    fun getMessagePagingSource(chatId: Int) = messageDAO.getMessagePagingSource(chatId)

    suspend fun clearMessages(chatId: Int) = messageDAO.clearChat(chatId)

    suspend fun insertMessages(items: List<MessageItem>) = messageDAO.insert(items)

    suspend fun getCount(chatId: Int) = messageDAO.getCount(chatId)

    suspend fun insertRemoteKeys(items: List<RemoteKeys>) = remoteKeysDAO.insert(items)

    suspend fun getRemoteKeys(messageId: Int) = remoteKeysDAO.getRemoteKeys(messageId)

    suspend fun clearRemoteKeys(chatId: Int) = remoteKeysDAO.clearRemoteKeys(chatId)
}