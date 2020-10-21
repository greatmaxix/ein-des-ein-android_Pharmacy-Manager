package com.pharmacy.manager.components.chatList.repository

import com.pharmacy.manager.components.chatList.model.chat.ChatItem

class ChatListRepository(
    private val rds: ChatListRemoteDataSource,
    private val lds: ChatListLocalDataSource
) {

    suspend fun chatList(page: Int, pageSize: Int) = rds.chatList(page, pageSize)

    fun getChatsPagingSource() = lds.getChatsPagingSource()

    suspend fun getRemoteKeys(chatId: Int) = lds.getRemoteKeys(chatId)

    suspend fun clearChats() = lds.clearMessages()

    suspend fun insertChatsWithKeys(chats: List<ChatItem>, page: Int) = lds.insertChatsWithKeys(chats, page)
}