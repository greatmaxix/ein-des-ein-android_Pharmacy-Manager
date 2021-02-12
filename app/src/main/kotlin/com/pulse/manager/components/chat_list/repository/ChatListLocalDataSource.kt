package com.pulse.manager.components.chat_list.repository

import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.components.chat_list.model.chat.ChatItemDAO
import com.pulse.manager.components.chat_list.model.remoteKeys.ChatsRemoteKeys.Companion.createRemoteKey
import com.pulse.manager.components.chat_list.model.remoteKeys.ChatsRemoteKeysDAO

class ChatListLocalDataSource(
    private val chatsRemoteKeysDAO: ChatsRemoteKeysDAO,
    private val chatItemDAO: ChatItemDAO,
) {

    fun getChatsPagingSource() = chatItemDAO.getChatsPagingSource()

    fun searchChatsPagingSource(query: String) = chatItemDAO.searchChatsPagingSource("%$query%")

    suspend fun getRemoteKeys(chatId: Int) = chatsRemoteKeysDAO.getRemoteKeys(chatId)

    suspend fun clearMessages() {
        chatItemDAO.clear()
        chatsRemoteKeysDAO.clearRemoteKeys()
    }

    suspend fun insertChatsWithKeys(chats: List<ChatItem>, page: Int) {
        val keys = chats.map { chatItem -> createRemoteKey(chatItem, page) }
        chatItemDAO.insert(chats)
        chatsRemoteKeysDAO.insert(keys)
    }

    suspend fun getLastChat() = chatItemDAO.getLastChat()
}