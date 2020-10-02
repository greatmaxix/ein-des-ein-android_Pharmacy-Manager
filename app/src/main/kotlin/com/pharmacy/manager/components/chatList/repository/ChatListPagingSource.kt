package com.pharmacy.manager.components.chatList.repository

import androidx.paging.PagingSource
import com.pharmacy.manager.components.chatList.model.ChatItem
import com.pharmacy.manager.data.GeneralErrorHandler
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChatListPagingSource : PagingSource<Int, ChatItem>(), KoinComponent {

    private val repository: ChatListRepository by inject()
    private val errorHandler by lazy { GeneralErrorHandler() }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatItem> = try {
        val response = repository.chatList(params.key ?: 1, params.loadSize)
        LoadResult.Page(response.data.items, null, response.data.currentPageNumber + 1)
    } catch (e: Exception) {
        LoadResult.Error(errorHandler.checkThrowable(e))
    }
}