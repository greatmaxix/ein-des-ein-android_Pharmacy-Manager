package com.pulse.manager.components.chat_list.model.chat

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.pulse.manager.data.local.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatItemDAO : BaseDao<ChatItem> {

    @Query("SELECT * FROM ChatItem ORDER BY createdAt DESC")
    fun getChatsPagingSource(): PagingSource<Int, ChatItem>

    @Query("SELECT * FROM ChatItem WHERE customerName LIKE :query ORDER BY createdAt DESC")
    fun searchChatsPagingSource(query: String): PagingSource<Int, ChatItem>

    @Query("DELETE FROM ChatItem")
    suspend fun clear()

    @Query("SELECT * FROM ChatItem WHERE id = :chatId  LIMIT 1")
    suspend fun getChat(chatId: Int): ChatItem?

    @Query("SELECT * FROM ChatItem WHERE id = :chatId  LIMIT 1")
    fun getChatFlow(chatId: Int): Flow<ChatItem?>

    @Query("SELECT * FROM ChatItem WHERE status LIKE '%opened%'")
    fun getOpenedChatsFlow(): Flow<List<ChatItem>?>

    @Query("SELECT * FROM ChatItem ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastChat(): ChatItem?
}