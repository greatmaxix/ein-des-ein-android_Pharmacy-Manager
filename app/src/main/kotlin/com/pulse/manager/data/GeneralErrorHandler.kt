package com.pulse.manager.data

import com.google.gson.Gson
import com.pulse.manager.R
import com.pulse.manager.data.rest.ErrorModel
import com.pulse.manager.data.rest.response.Violation
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@OptIn(KoinApiExtension::class)
class GeneralErrorHandler : KoinComponent {

    fun checkThrowable(throwable: Throwable) = when (throwable) {
        is SocketException, is UnknownHostException, is SocketTimeoutException -> networkError
        is HttpException -> httpError(throwable)
        is GeneralException -> throwable
        else -> unknownError
    }.also { Timber.e(throwable) }

    private val networkError
        get() = GeneralException("network", R.string.error_serverNotResponding)

    private val unknownError
        get() = GeneralException("unknown", R.string.error_errorGettingData)

    private fun <T> errorBody(error: HttpException, type: Class<T>) =
        error.response()?.errorBody()?.run { get<Gson>().fromJson(string(), type) }

    //TODO have to add valid response for errors
    private fun httpError(error: HttpException): GeneralException {
        val errorModel = errorBody(error, ErrorModel::class.java)
        return when (errorModel?.type) {
            "validation_error" -> wrapViolations(errorModel.violations.first())
            else -> unknownError
        }
    }

    private fun wrapViolations(violation: Violation) = when (violation.message) {
        else -> unknownError
    }
}