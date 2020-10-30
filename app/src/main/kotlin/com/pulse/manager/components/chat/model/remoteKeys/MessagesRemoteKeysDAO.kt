package com.pulse.manager.components.chat.model.remoteKeys

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Query
import com.pulse.manager.data.local.db.BaseDao

@Dao
interface MessagesRemoteKeysDAO : BaseDao<MessagesRemoteKeys> {

    @WorkerThread
    @Query("SELECT * FROM MessagesRemoteKeys WHERE messageId = :messageId")
    suspend fun getRemoteKeys(messageId: Int): MessagesRemoteKeys?

    @WorkerThread
    @Query("DELETE FROM MessagesRemoteKeys WHERE chatId = :chatId")
    suspend fun clearRemoteKeys(chatId: Int)

    @Query("DELETE FROM MessagesRemoteKeys WHERE chatId = :chatId AND messageId = :messageId")
    suspend fun deleteById(chatId: Int, messageId: Int)
}