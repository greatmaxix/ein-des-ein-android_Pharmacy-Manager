package com.pulse.manager.components.product.model

import android.os.Parcelable
import androidx.paging.ExperimentalPagingApi
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("activeSubstances") val substances: List<String>
) : ProductLite(), Parcelable {

    val substance
        get() = substances.first()

    @ExperimentalPagingApi
    val getFullManufacture
        get() = "${manufacture.producer} , $productLocale"
}