package com.pulse.manager.components.chatList.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.pulse.manager.components.chatList.model.chat.ChatItem
import com.pulse.manager.components.chatList.model.remoteKeys.ChatsRemoteKeys
import com.pulse.manager.data.GeneralErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalPagingApi
class ChatListRemoteMediator(private val repository: ChatListRepository, private val errorHandler: GeneralErrorHandler) : RemoteMediator<Int, ChatItem>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ChatItem>): MediatorResult = withContext(Dispatchers.IO) {
        try {
            val remoteKeys = getRemoteKeys(loadType, state)
            if (!remoteKeys.isError) {
                val page = remoteKeys.nextPage ?: 1
                val items = repository.chatList(page, state.config.pageSize)
                    .items

                if (items.isNotEmpty()) {
                    if (loadType == LoadType.REFRESH) repository.clearChats()
                    repository.insertChatsWithKeys(items, page)

                    return@withContext MediatorResult.Success(endOfPaginationReached = items.isEmpty())
                }
            }
            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            MediatorResult.Error(errorHandler.checkThrowable(e))
        }
    }

    private suspend fun getRemoteKeys(loadType: LoadType, state: PagingState<Int, ChatItem>): ChatsRemoteKeys {
        return when (loadType) {
            LoadType.REFRESH -> getRemoteKeyClosestToCurrentPosition(state) ?: ChatsRemoteKeys.emptyInstance()
            LoadType.PREPEND -> ChatsRemoteKeys.errorInstance()
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextPage == null) ChatsRemoteKeys.errorInstance() else remoteKeys
            }
            else -> ChatsRemoteKeys.emptyInstance()
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ChatItem>): ChatsRemoteKeys? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { chat -> repository.getRemoteKeys(chat.id) }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ChatItem>): ChatsRemoteKeys? =
        state.anchorPosition?.let { position -> state.closestItemToPosition(position)?.id?.let { chatId -> repository.getRemoteKeys(chatId) } }
}