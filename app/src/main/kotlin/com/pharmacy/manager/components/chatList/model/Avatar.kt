package com.pharmacy.manager.components.chatList.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Avatar(@SerializedName("url") val url: String) : Parcelable