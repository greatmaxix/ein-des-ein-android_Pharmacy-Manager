package com.pulse.manager.components.signIn.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pulse.manager.components.chat_list.model.AvatarShort

@Entity
data class User(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("type") val type: String,
    @SerializedName("topicName") val topicName: String?,
    @SerializedName("aboutMe") val aboutMe: String?,
    @Embedded(prefix = "avatar")
    @SerializedName("avatar") val avatar: AvatarShort? = null,
    @Embedded
    @SerializedName("chatRatingInfo") val chatRatingInfo: ChatRatingInfo?
)