package com.pulse.manager.data.rest.interceptor

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.manager.core.extensions.getOnes
import com.pulse.manager.data.local.Preferences.Token.FIELD_ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()
        with(newRequestBuilder) {
            header(CONTENT_TYPE, APPLICATION_JSON)
            val token = dataStore.getOnes(FIELD_ACCESS_TOKEN, "")
            if (request.headers[HEADER_IGNORE_AUTHORIZATION].isNullOrBlank() && token.isNotBlank()) {
                header(AUTHORIZATION, "$BEARER $token")
            }
        }

        return chain.proceed(newRequestBuilder.build())
    }

    companion object {

        const val HEADER_IGNORE_AUTHORIZATION = "IgnoreAuthorization: true"
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
        private const val CONTENT_TYPE = "Content-Type"
        private const val APPLICATION_JSON = "application/json"
    }
}