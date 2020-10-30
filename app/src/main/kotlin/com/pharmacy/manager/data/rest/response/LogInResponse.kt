package com.pharmacy.manager.data.rest.response

import com.google.gson.annotations.SerializedName
import com.pharmacy.manager.components.signIn.model.User

data class LogInResponse(
    @SerializedName("item") val item: User,
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String
)