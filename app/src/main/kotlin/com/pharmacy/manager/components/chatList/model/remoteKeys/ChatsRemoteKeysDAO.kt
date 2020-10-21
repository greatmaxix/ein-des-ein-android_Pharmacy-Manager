package com.pharmacy.manager.components.chatList.model.remoteKeys

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.manager.data.local.db.BaseDao

@Dao
interface ChatsRemoteKeysDAO : BaseDao<ChatsRemoteKeys> {

    @WorkerThread
    @Query("SELECT * FROM ChatsRemoteKeys WHERE chatId = :chatId")
    suspend fun getRemoteKeys(chatId: Int): ChatsRemoteKeys?

    @WorkerThread
    @Query("DELETE FROM ChatsRemoteKeys")
    suspend fun clearRemoteKeys()
}