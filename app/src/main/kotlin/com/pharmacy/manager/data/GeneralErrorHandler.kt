package com.pharmacy.manager.data

import com.google.gson.Gson
import com.pharmacy.manager.R
import org.koin.core.KoinComponent
import org.koin.core.get
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GeneralErrorHandler : KoinComponent {

    fun checkThrowable(throwable: Throwable) = when (throwable) {
        is SocketException, is UnknownHostException, is SocketTimeoutException -> networkError
        is HttpException -> httpError(throwable)
        is GeneralException -> throwable
        else -> unknownError
    }.also { Timber.e(throwable) }

    private val networkError
        get() = GeneralException("network", R.string.error_network)

    private val unknownError
        get() = GeneralException("unknown", R.string.error_unknown)

    private fun httpError(error: HttpException) =
        GeneralException(error.message())

    private fun <T> errorBody(error: HttpException, type: Class<T>) =
        error.response()?.errorBody()?.run { get<Gson>().fromJson(string(), type) }
}