package com.pulse.manager.components.chat.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.pulse.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_DATE_HEADER
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.components.chat.model.remoteKeys.MessagesRemoteKeys
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.data.GeneralErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import java.time.ZoneOffset

@KoinApiExtension
@ExperimentalPagingApi
class ChatMessagesRemoteMediator(private val repository: ChatRepository, private val errorHandler: GeneralErrorHandler, private val chat: ChatItem) :
    RemoteMediator<Int, MessageItem>() {

    private var userUuid: String? = null

    override suspend fun initialize(): InitializeAction =
        if (repository.getLastMessage(chat.id) == null) InitializeAction.LAUNCH_INITIAL_REFRESH else InitializeAction.SKIP_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MessageItem>): MediatorResult = withContext(Dispatchers.IO) {
        try {
            val remoteKeys = getRemoteKeys(loadType, state)
            if (!remoteKeys.isError) {
                if (userUuid == null) userUuid = repository.getUserUuid()
                val beforeNumber: Int? = null
                var afterNumber: Int? = null

                val startNumber = chat.lastMessage?.messageNumber
                if (loadType == LoadType.REFRESH && startNumber != null && startNumber > state.config.pageSize) {
                    afterNumber = startNumber - state.config.pageSize
                } else if (loadType == LoadType.APPEND && remoteKeys.nextNumber != null) afterNumber = remoteKeys.nextNumber - 1

                val response = repository.fetchMessagesList(chat.id, state.config.pageSize, afterNumber, beforeNumber)
                var items = response.items.apply { forEach { it.updateMessageType(userUuid) } }
                if (items.isNotEmpty()) {
                    val messagesList = arrayListOf<MessageItem>()
                    items = items.sortedBy { it.createdAt.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() }
                    var lastItem = if (loadType != LoadType.REFRESH) repository.getLastMessage(chat.id) else null
                    items.forEachIndexed { index, messageItem ->
                        if ((index == 0 && lastItem == null) || (lastItem != null && lastItem?.createdAt?.dayOfMonth != messageItem.createdAt.dayOfMonth)) {
                            val header = MessageItem.getStubItem(null, messageItem, TYPE_DATE_HEADER, messageItem.chatId)
                            if (!repository.isHeaderExist(chat.id, header.createdAt)) messagesList.add(header)
                        }
                        messagesList.add(messageItem)
                        lastItem = messageItem
                    }
                    if (loadType == LoadType.REFRESH) repository.clearMessages(chat.id)
                    repository.insertMessagesWithKeys(messagesList)

                    return@withContext MediatorResult.Success(endOfPaginationReached = messagesList.size < state.config.pageSize)
                }
            }
            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            MediatorResult.Error(errorHandler.checkThrowable(e))
        }
    }

    private suspend fun getRemoteKeys(loadType: LoadType, state: PagingState<Int, MessageItem>): MessagesRemoteKeys {
        return when (loadType) {
            LoadType.REFRESH -> getRemoteKeyClosestToCurrentPosition(state) ?: MessagesRemoteKeys.emptyInstance()
            LoadType.PREPEND -> MessagesRemoteKeys.errorInstance()
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys?.nextNumber == null) MessagesRemoteKeys.errorInstance() else remoteKeys
            }
            else -> MessagesRemoteKeys.emptyInstance()
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MessageItem>): MessagesRemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull { it.messageNumber != -1 }?.let { message -> repository.getRemoteKeys(message.id) }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MessageItem>): MessagesRemoteKeys? =
        state.anchorPosition?.let { position -> state.closestItemToPosition(position)?.id?.let { messageId -> repository.getRemoteKeys(messageId) } }
}