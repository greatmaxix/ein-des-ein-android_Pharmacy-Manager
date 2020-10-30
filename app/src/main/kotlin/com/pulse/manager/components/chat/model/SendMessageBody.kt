package com.pulse.manager.components.chat.model

import com.google.gson.annotations.SerializedName

data class SendMessageBody(
    @SerializedName("text") val text: String
)