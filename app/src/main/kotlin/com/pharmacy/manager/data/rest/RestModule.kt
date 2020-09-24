package com.pharmacy.manager.data.rest

import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.pharmacy.manager.BuildConfig
import com.pharmacy.manager.core.network.FlowCallAdapterFactory
import com.pharmacy.manager.data.rest.deserializer.StringDeserializer
import com.pharmacy.manager.data.rest.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.pharmacies.fmc-dev.com" /* "https://api.pharmacies.release.fmc-dev.com" */ // TODO change to release in future
private const val READ_TIMEOUT = 30L
private const val CONNECT_TIMEOUT = 60L
private const val WRITE_TIMEOUT = 120L

val restModule = module {

    single { get<Retrofit>().create(RestApi::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(FlowCallAdapterFactory.create)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    fun makeLoggingInterceptor() = LoggingInterceptor.Builder()
        .setLevel(Level.BASIC)
        .log(Platform.INFO)
        .tag("OkHttp")
        .build()

    single {
        OkHttpClient.Builder().apply {
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            with(interceptors()) {
                add(HeaderInterceptor())
                if (BuildConfig.DEBUG) {
                    add(makeLoggingInterceptor())
                }
            }
        }.build()
    }

    single {
        GsonBuilder().apply {
            setLenient()
            registerTypeAdapter(String::class.java, StringDeserializer())
        }.create()
    }
}