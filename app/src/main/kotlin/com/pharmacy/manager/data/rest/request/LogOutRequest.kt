package com.pharmacy.manager.data.rest.request

import com.google.gson.annotations.SerializedName

data class LogOutRequest(@SerializedName("refreshToken") val refreshToken: String)