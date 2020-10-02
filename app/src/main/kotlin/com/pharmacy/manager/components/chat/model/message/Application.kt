package com.pharmacy.manager.components.chat.model.message

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Application(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("file") val file: String,
) : Parcelable