package com.pulse.manager.components.chat.model.message

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import com.pulse.manager.components.product.model.Aggregation
import com.pulse.manager.components.product.model.Picture
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageProduct(
    @SerializedName("globalProductId") val globalProductId: Int,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("pictures") val pictures: List<Picture> = listOf(),
    @Embedded
    @SerializedName("pharmacyProductsAggregationData") val pharmacyProductsAggregationData: Aggregation?
) : Parcelable