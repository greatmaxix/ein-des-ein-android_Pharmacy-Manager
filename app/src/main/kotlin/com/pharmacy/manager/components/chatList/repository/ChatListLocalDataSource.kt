package com.pharmacy.manager.components.chatList.repository

import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.components.chatList.model.chat.ChatItemDAO
import com.pharmacy.manager.components.chatList.model.remoteKeys.ChatsRemoteKeys.Companion.createRemoteKey
import com.pharmacy.manager.components.chatList.model.remoteKeys.ChatsRemoteKeysDAO

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
}