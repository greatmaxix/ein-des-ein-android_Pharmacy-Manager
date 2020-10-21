package com.pharmacy.manager.components.chatList.repository

import com.pharmacy.manager.data.rest.RestApi

class ChatListRemoteDataSource(private val ra: RestApi) {

    suspend fun chatList(page: Int?, pageSize: Int?) = ra.chatList(page, pageSize)
        .dataOrThrow()
}