package com.pulse.manager.components.chatList.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AvatarShort(@SerializedName("url") val url: String?, @SerializedName("uuid") val uuid: String?) : Parcelable