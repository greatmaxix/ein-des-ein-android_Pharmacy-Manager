package com.pulse.manager.components.chat_list.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AvatarShort(@SerializedName("url") val url: String?, @SerializedName("uuid") val uuid: String?) : Parcelable