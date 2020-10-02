package com.pharmacy.manager.components.chat.model.message

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pharmacy.manager.components.chatList.model.UserShort
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class MessageItem(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @Embedded(prefix = "baseUser")
    @SerializedName("baseUser") val baseUser: UserShort,
    @SerializedName("text") val text: String?,
    @SerializedName("applications") val applications: List<Application> = listOf(),
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("chatId") var chatId: Int? = null,
    @SerializedName("messageType") var messageType: Int? = null
) : Parcelable