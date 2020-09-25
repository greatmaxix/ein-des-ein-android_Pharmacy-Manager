package com.pharmacy.manager.components.chat.repository

class ChatRepository(
    private val rds: ChatRemoteDataSource,
    private val lds: ChatLocalDataSource
)