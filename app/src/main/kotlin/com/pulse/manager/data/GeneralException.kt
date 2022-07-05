package com.pulse.manager.data

import androidx.annotation.StringRes
import com.pulse.manager.R

data class GeneralException(override val message: String, @StringRes val resId: Int = -1, val data: Any? = null) : Throwable() {

    companion object {
        fun someException() = GeneralException("some exception", R.string.error_networkErrorMessage)
    }
}