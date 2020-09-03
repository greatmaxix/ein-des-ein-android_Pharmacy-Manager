package com.pharmacy.manager.data

import androidx.annotation.StringRes

data class GeneralException(
    override val message: String,
    @StringRes val resId: Int = -1,
    val data: Any? = null
) : Throwable()