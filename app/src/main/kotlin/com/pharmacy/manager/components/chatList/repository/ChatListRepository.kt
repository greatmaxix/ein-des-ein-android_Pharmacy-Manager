package com.pharmacy.manager.components.chatList.repository

class ChatListRepository(private val rds: ChatListRemoteDataSource) {

    suspend fun chatList(page: Int, pageSize: Int) = rds.chatList(page, pageSize)
}