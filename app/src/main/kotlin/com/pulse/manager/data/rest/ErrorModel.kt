package com.pulse.manager.data.rest

import com.google.gson.annotations.SerializedName
import com.pulse.manager.data.rest.response.Violation

data class ErrorModel(
    @SerializedName("message") val message: String,
    @SerializedName("error_type") val type: String,
    @SerializedName("violations") val violations: List<Violation>
)