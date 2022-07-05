package com.pulse.manager.data.rest.response

import com.google.gson.annotations.SerializedName
import com.pulse.manager.components.signIn.model.User

data class LogInResponse(
    @SerializedName("item") val item: User,
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String
)