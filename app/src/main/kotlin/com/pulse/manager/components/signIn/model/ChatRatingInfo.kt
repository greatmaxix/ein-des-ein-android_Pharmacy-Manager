
package com.pulse.manager.components.signIn.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRatingInfo(
    @SerializedName("rating") val rating: Float,
    @SerializedName("count") val count: Int
) : Parcelable
