package com.pulse.manager.components.chatList.model.remoteKeys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pulse.manager.components.chatList.model.chat.ChatItem

@Entity
data class ChatsRemoteKeys(
    @PrimaryKey val chatId: Int,
    val prevPage: Int?,
    val nextPage: Int?
) {

    val isError: Boolean
        get() = chatId == -1

    companion object {

        fun emptyInstance() = ChatsRemoteKeys(0, null, null)

        fun errorInstance() = ChatsRemoteKeys(-1, null, null)

        fun createRemoteKey(chat: ChatItem, page: Int): ChatsRemoteKeys {
            return ChatsRemoteKeys(chatId = chat.id, prevPage = if (page == 1) null else page - 1, nextPage = page + 1)
        }
    }
}