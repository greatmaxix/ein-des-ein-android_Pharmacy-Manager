package com.pharmacy.manager.components.chatList.model.chat

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.manager.data.local.db.BaseDao

@Dao
interface ChatItemDAO : BaseDao<ChatItem> {

    @Query("SELECT * FROM ChatItem ORDER BY createdAt DESC")
    fun getChatsPagingSource(): PagingSource<Int, ChatItem>

    @Query("DELETE FROM ChatItem")
    suspend fun clear()

    @Query("SELECT * FROM ChatItem WHERE id = :chatId  LIMIT 1")
    suspend fun getChat(chatId: Int): ChatItem?
}