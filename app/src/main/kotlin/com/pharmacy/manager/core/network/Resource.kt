package com.pharmacy.manager.core.network

import com.pharmacy.manager.data.GeneralException

sealed class Resource<out T> {

    data class Error(val exception: GeneralException) : Resource<Nothing>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Progress(val isLoading: Boolean = true) : Resource<Nothing>()
}