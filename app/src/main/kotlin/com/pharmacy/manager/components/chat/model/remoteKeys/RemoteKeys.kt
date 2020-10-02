package com.pharmacy.manager.components.chat.model.remoteKeys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
        @PrimaryKey val messageId: Int,
        val chatId: Int,
        val prevKey: Int?,
        val nextKey: Int?
)