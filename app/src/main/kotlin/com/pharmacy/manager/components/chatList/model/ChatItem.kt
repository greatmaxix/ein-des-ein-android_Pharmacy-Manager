package com.pharmacy.manager.components.chatList.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.manager.components.chat.model.message.MessageItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatItem(
    @SerializedName("id") val id: Int,
    @SerializedName("topicName") val topicName: String,
    @SerializedName("customer") val customer: UserShort,
    @SerializedName("status") val status: String,
    @SerializedName("mark") val mark: Int,
    @SerializedName("type") val type: String,
    @SerializedName("user") val user: UserShort,
    @SerializedName("lastMessage") val lastMessage: MessageItem
) : Parcelable