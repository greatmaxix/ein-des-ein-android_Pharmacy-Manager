package com.pulse.manager.data.rest

import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.pulse.manager.BuildConfig.*
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.data.rest.interceptor.HeaderInterceptor
import com.pulse.manager.data.rest.serializer.CategoryDeserializer
import com.pulse.manager.data.rest.serializer.DateTimeSerializer
import com.pulse.manager.data.rest.serializer.StringDeserializer
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

private const val DEV_BASE_URL = "https://api.pharmacies.fmc-dev.com"
private const val RELEASE_BASE_URL = "https://api.pharmacies.release.fmc-dev.com"
private const val READ_TIMEOUT = 30L
private const val CONNECT_TIMEOUT = 60L
private const val WRITE_TIMEOUT = 120L

val restModule = module {

    single { get<Retrofit>().create(RestApi::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl(if (DEVELOPER_SERVER) DEV_BASE_URL else RELEASE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single { HeaderInterceptor(get()) }

    fun makeLoggingInterceptor() = LoggingInterceptor.Builder()
        .setLevel(Level.BASIC)
        .log(Platform.INFO)
        .tag("OkHttp")
        .build()

    fun makeOkHttpLoggingInterceptor() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    single {
        OkHttpClient.Builder().apply {
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            with(interceptors()) {
                add(get<HeaderInterceptor>())
                add(if (DEBUG && IHSANBAL) makeLoggingInterceptor() else makeOkHttpLoggingInterceptor())
            }
        }.build()
    }

    single {
        GsonBuilder().apply {
            setLenient()
            registerTypeAdapter(String::class.java, StringDeserializer())
            registerTypeAdapter(LocalDateTime::class.java, DateTimeSerializer())
            registerTypeAdapter(Category::class.java, CategoryDeserializer())
        }.create()
    }
}