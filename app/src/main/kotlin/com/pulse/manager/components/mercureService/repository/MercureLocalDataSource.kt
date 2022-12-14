package com.pulse.manager.components.mercureService.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.manager.components.chat.model.message.MessageDAO
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.components.chat.model.remoteKeys.MessagesRemoteKeys.Companion.createRemoteKey
import com.pulse.manager.components.chat.model.remoteKeys.MessagesRemoteKeysDAO
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.components.chat_list.model.chat.ChatItemDAO
import com.pulse.manager.components.chat_list.model.remoteKeys.ChatsRemoteKeys
import com.pulse.manager.components.chat_list.model.remoteKeys.ChatsRemoteKeysDAO
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.core.extensions.getOnes
import com.pulse.manager.data.local.Preferences.Chat.FIELD_IS_CHAT_FOREGROUND
import java.time.LocalDateTime

class MercureLocalDataSource(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDAO,
    private val messagesRemoteKeysDAO: MessagesRemoteKeysDAO,
    private val messageDAO: MessageDAO,
    private val chatsRemoteKeysDAO: ChatsRemoteKeysDAO,
    private val chatItemDAO: ChatItemDAO,
) {

    val isChatForeground: Boolean
        get() = dataStore.getOnes(FIELD_IS_CHAT_FOREGROUND,false)

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

    suspend fun clearEndChatMessage(chatId: Int) = messageDAO.getEndChatMessage(chatId)
        ?.let {
            messagesRemoteKeysDAO.deleteById(chatId, it.id)
            messageDAO.delete(it)
        }

    suspend fun getChat(chatId: Int) = chatItemDAO.getChat(chatId)
}