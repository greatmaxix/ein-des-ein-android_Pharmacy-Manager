package com.pulse.manager.data.rest.request

import com.google.gson.annotations.SerializedName

data class LogInRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)