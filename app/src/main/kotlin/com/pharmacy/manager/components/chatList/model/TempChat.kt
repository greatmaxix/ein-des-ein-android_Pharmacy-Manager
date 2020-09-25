package com.pharmacy.manager.components.chatList.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TempChat(
    val avatar: String,
    val name: String,
    val lastMessage: String,
    val messagesCount: Int,
    val time: String
) : Parcelable