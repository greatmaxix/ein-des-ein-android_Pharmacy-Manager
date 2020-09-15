package com.pharmacy.manager.data.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.pharmacy.manager.BuildConfig
import com.pharmacy.manager.core.network.FlowCallAdapterFactory
import com.pharmacy.manager.data.rest.deserializer.StringDeserializer
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val restModule = module {

    single { get<Retrofit>().create(RestApi::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host") // TODO set proper url
            .addCallAdapterFactory(FlowCallAdapterFactory.create)
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .client(get())
            .build()
    }

    fun makeLoggingInterceptor() = LoggingInterceptor.Builder()
        .setLevel(Level.BASIC)
        .log(Platform.INFO)
        .tag("OkHttp")
        .build()

    single {
        val timeOut = 20L
        OkHttpClient.Builder().apply {
            readTimeout(timeOut, TimeUnit.SECONDS)
            writeTimeout(timeOut, TimeUnit.SECONDS)
            connectTimeout(timeOut, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                interceptors().add(makeLoggingInterceptor())
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