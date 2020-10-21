package com.pharmacy.manager.components.mercureService.repository

import com.pharmacy.manager.components.chat.model.message.MessageDAO
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chat.model.remoteKeys.MessagesRemoteKeys.Companion.createRemoteKey
import com.pharmacy.manager.components.chat.model.remoteKeys.MessagesRemoteKeysDAO
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.components.chatList.model.chat.ChatItemDAO
import com.pharmacy.manager.components.chatList.model.remoteKeys.ChatsRemoteKeys
import com.pharmacy.manager.components.chatList.model.remoteKeys.ChatsRemoteKeysDAO
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager
import java.time.LocalDateTime

class MercureLocalDataSource(
    private val sp: SPManager,
    private val userDao: UserDAO,
    private val messagesRemoteKeysDAO: MessagesRemoteKeysDAO,
    private val messageDAO: MessageDAO,
    private val chatsRemoteKeysDAO: ChatsRemoteKeysDAO,
    private val chatItemDAO: ChatItemDAO,
) {

    val isChatForeground: Boolean
        get() = sp.isChatForeground ?: false

    suspend fun getUserUuid() = userDao.getUser()?.uuid

    suspend fun getTopicName() = userDao.getUser()?.topicName

    suspend fun insertMessageWithKeys(message: MessageItem) {
        messageDAO.insert(message)
        messagesRemoteKeysDAO.insert(createRemoteKey(message))
        chatItemDAO.getChat(message.chatId)?.let {
            val chat = it.copy(lastMessage = message)
            chatItemDAO.update(chat)
        }
    }

    suspend fun getLastMessage(chatId: Int) = messageDAO.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = messageDAO.getHeaderMessages(chatId)
        .find { it.createdAt.year == createdAt.year && it.createdAt.month == createdAt.month && it.createdAt.dayOfMonth == createdAt.dayOfMonth } != null

    suspend fun insertChatWithKeys(chat: ChatItem) {
        val keys = ChatsRemoteKeys.createRemoteKey(chat, 1)
        chatItemDAO.insert(chat)
        chatsRemoteKeysDAO.insert(keys)
    }
}