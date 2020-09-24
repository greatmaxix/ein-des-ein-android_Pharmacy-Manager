package com.pharmacy.manager.components.signIn.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("type") val type: String
)