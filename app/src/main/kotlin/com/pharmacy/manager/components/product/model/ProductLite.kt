package com.pharmacy.manager.components.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.manager.App
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class ProductLite(
    @SerializedName("globalProductId") val globalProductId: Int = -1,
    @SerializedName("rusName") val rusName: String = "",
    @SerializedName("releaseForm") val releaseForm: String = "",
    @SerializedName("pictures") val pictures: List<Picture> = listOf(),
    @SerializedName("manufacturerData") val manufacture: Manufacture = Manufacture(),
    @SerializedName("pharmacyProductsAggregationData") val aggregation: Aggregation? = Aggregation()
) : Parcelable {

    //TODO Create global "Local helper"
    val productLocale: String?
        get() = App.localeMap[manufacture.isoCode]?.getDisplayCountry(Locale("RU"))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductLite

        if (globalProductId != other.globalProductId) return false
        if (rusName != other.rusName) return false
        if (releaseForm != other.releaseForm) return false
        if (pictures != other.pictures) return false
        if (manufacture != other.manufacture) return false
        if (aggregation != other.aggregation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = globalProductId
        result = 31 * result + rusName.hashCode()
        result = 31 * result + releaseForm.hashCode()
        result = 31 * result + pictures.hashCode()
        result = 31 * result + manufacture.hashCode()
        result = 31 * result + aggregation.hashCode()
        return result
    }
}