package com.pharmacy.manager.components.chatList.repository

class ChatListRepository(
    private val rds: ChatListRemoteDataSource,
    private val lds: ChatListLocalDataSource
)