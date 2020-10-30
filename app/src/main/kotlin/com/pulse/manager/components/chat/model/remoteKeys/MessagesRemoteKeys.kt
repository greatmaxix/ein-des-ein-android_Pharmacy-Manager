package com.pulse.manager.components.chat.model.remoteKeys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pulse.manager.components.chat.model.message.MessageItem

@Entity
data class MessagesRemoteKeys(
    @PrimaryKey val messageId: Int,
    val chatId: Int,
    val prevNumber: Int?,
    val nextNumber: Int?
) {

    val isError: Boolean
        get() = messageId == -1 && chatId == -1

    companion object {

        fun emptyInstance() = MessagesRemoteKeys(0, 0, null, null)

        fun errorInstance() = MessagesRemoteKeys(-1, -1, null, null)

        fun createRemoteKey(message: MessageItem): MessagesRemoteKeys {
            val prevNumber = if (message.messageNumber <= 1) null else message.messageNumber - 1
            return MessagesRemoteKeys(messageId = message.id, chatId = message.chatId, prevNumber = prevNumber, nextNumber = message.messageNumber + 1)
        }
    }
}