package com.pharmacy.manager.components.chatList.model.chat

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chatList.model.UserShort
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity
data class ChatItem(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("topicName") val topicName: String,
    @Embedded
    @SerializedName("customer") val customer: UserShort,
    @SerializedName("status") val status: String,
    @ColumnInfo(name = "chatType")
    @SerializedName("type") val type: String,
    @Embedded
    @SerializedName("lastMessage") val lastMessage: MessageItem?,
    @SerializedName("createdAt") val createdAt: LocalDateTime?,
    @SerializedName("isAutomaticClosed") val isAutomaticClosed: Boolean
) : Parcelable {

    val isAbleToWrite: Boolean
        get() = status != STATUS_CLOSE_REQUEST && status != STATUS_CLOSED

    val isClosed: Boolean
        get() = status == STATUS_CLOSED

    companion object {

        const val STATUS_OPENED = "opened"
        const val STATUS_ANSWERED = "answered"
        const val STATUS_CLOSE_REQUEST = "close_request"
        const val STATUS_CLOSED = "closed"
    }
}