package com.pulse.manager.components.chatList.repository

import com.pulse.manager.data.rest.RestApi

class ChatListRemoteDataSource(private val ra: RestApi) {

    suspend fun chatList(page: Int?, pageSize: Int?) = ra.chatList(page, pageSize)
        .dataOrThrow()
}