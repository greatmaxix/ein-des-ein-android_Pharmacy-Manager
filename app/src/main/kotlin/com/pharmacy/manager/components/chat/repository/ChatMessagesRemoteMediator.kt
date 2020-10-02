package com.pharmacy.manager.components.chat.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chat.model.remoteKeys.RemoteKeys
import com.pharmacy.manager.data.GeneralErrorHandler
import timber.log.Timber
import java.io.InvalidObjectException

@ExperimentalPagingApi
class ChatMessagesRemoteMediator(private val repository: ChatRepository, private val errorHandler: GeneralErrorHandler, private val chatId: Int) :
    RemoteMediator<Int, MessageItem>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MessageItem>): MediatorResult = try {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state) ?: throw InvalidObjectException("Remote key and the \'prevKey\' should not be null")
                remoteKeys.prevKey ?: ERROR_PAGE
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            else -> DEFAULT_PAGE
        }

        if (page == ERROR_PAGE) {
            MediatorResult.Success(endOfPaginationReached = true)
        } else {
            val response = repository.messagesList(chatId, page, state.config.pageSize)

            if (loadType == LoadType.REFRESH) {
                repository.clearMessagesCache(chatId)
                repository.clearRemoteKeys(chatId)
            }
            val items = response.dataOrThrow().items
            val endOfPaginationReached = items.isEmpty()

            val prevKey = if (page == DEFAULT_PAGE) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = items.map {
                RemoteKeys(messageId = it.id, chatId = chatId, prevKey = prevKey, nextKey = nextKey)
            }
            Timber.e("keys >>> ${keys.joinToString { "$it, " }}")
            repository.insertRemoteKeys(keys)
            items.forEach {
                it.chatId = chatId
                it.messageType = when {
                    // TODO
//                        TYPE_MESSAGE_USER
//                        TYPE_MESSAGE_PHARMACY
//                        TYPE_DATE_HEADER
//                        TYPE_ATTACHMENT
//                        TYPE_PRODUCT

                    // TODO add header with date
                    it.baseUser.uuid == repository.getUserUuid() -> ChatMessageAdapter.TYPE_MESSAGE_USER
                    else -> ChatMessageAdapter.TYPE_MESSAGE_PHARMACY
                }
            }
            repository.insertMessages(items)

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }
    } catch (e: Exception) {
        MediatorResult.Error(errorHandler.checkThrowable(e))
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MessageItem>): RemoteKeys? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { message -> repository.getRemoteKeys(message.id) }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MessageItem>): RemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { message -> repository.getRemoteKeys(message.id) }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MessageItem>): RemoteKeys? =
        state.anchorPosition?.let { position -> state.closestItemToPosition(position)?.id?.let { messageId -> repository.getRemoteKeys(messageId) } }

    companion object {

        private const val DEFAULT_PAGE = 1
        private const val ERROR_PAGE = -1
    }
}