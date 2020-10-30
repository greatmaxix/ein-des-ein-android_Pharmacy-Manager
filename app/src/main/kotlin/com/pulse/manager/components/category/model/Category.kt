package com.pulse.manager.components.category.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("nodes") val nodes: List<Category>
)