package com.pharmacy.manager.components.chat.model.message

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.manager.data.local.db.BaseDao

@Dao
interface MessageDAO : BaseDao<MessageItem> {

    @Query("SELECT * FROM messageitem WHERE chatId = :chatId")
    fun getMessagePagingSource(chatId: Int): PagingSource<Int, MessageItem>

    @Query("DELETE FROM messageitem WHERE chatId = :chatId")
    suspend fun clearChat(chatId: Int)

    @Query("SELECT COUNT(id) FROM messageitem WHERE chatId = :chatId")
    suspend fun getCount(chatId: Int): Int
}