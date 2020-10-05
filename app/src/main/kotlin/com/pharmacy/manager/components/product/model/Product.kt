package com.pharmacy.manager.components.product.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class Product(
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("activeSubstances") val substances: List<String>
) : ProductLite(), Parcelable {

    val substance
        get() = substances.first()

    val getFullManufacture
        get() = "${manufacture.producer} , $productLocale"
}