package com.pulse.manager.components.product.model

import android.os.Parcelable
import androidx.paging.ExperimentalPagingApi
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pulse.manager.App
import kotlinx.coroutines.FlowPreview
import kotlinx.parcelize.Parcelize
import org.koin.core.KoinExperimentalAPI
import java.util.*

@Parcelize
@Entity
open class ProductLite(
    @SerializedName("globalProductId") var globalProductId: Int = -1,
    @SerializedName("rusName") var rusName: String = "",
    @SerializedName("releaseForm") var releaseForm: String = "",
    @SerializedName("pictures") var pictures: List<Picture> = listOf(),
    @Embedded(prefix = "manufacturerData")
    @SerializedName("manufacturerData") var manufacture: Manufacture = Manufacture(),
    @Embedded(prefix = "pharmacyProductsAggregationData")
    @SerializedName("pharmacyProductsAggregationData") var aggregation: Aggregation? = Aggregation(),
    @PrimaryKey
    @Expose(serialize = false)
    var primaryKey: Int = 0
) : Parcelable {

    //TODO Create global "Local helper"
    @FlowPreview
    @KoinExperimentalAPI
    @ExperimentalPagingApi
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