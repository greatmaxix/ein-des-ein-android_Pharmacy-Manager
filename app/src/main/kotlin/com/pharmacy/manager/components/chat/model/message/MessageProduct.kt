package com.pharmacy.manager.components.chat.model.message

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import com.pharmacy.manager.components.product.model.Aggregation
import com.pharmacy.manager.components.product.model.Picture
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageProduct(
    @SerializedName("globalProductId") val globalProductId: Int,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("pictures") val pictures: List<Picture> = listOf(),
    @Embedded
    @SerializedName("pharmacyProductsAggregationData") val pharmacyProductsAggregationData: Aggregation?
) : Parcelable