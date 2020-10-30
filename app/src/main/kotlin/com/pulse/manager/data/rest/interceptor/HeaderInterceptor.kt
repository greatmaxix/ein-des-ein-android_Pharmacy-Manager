package com.pulse.manager.data.rest.interceptor

import com.pulse.manager.data.local.SPManager
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val sp: SPManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()
        with(newRequestBuilder) {
            header(CONTENT_TYPE, APPLICATION_JSON)
            val token = sp.accessToken
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