package com.pharmacy.manager.components.mercureService.model

import com.google.gson.annotations.SerializedName

data class MercureResponse(
    @SerializedName("type") val type: String,
    @SerializedName("body") val body: String
)