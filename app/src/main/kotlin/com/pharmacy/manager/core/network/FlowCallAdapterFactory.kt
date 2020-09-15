package com.pharmacy.manager.core.network

import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        val create by lazy { FlowCallAdapterFactory() }
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit) =
        if (getRawType(returnType) == Flow::class.java) {
            getParameterUpperBound(0, returnType as ParameterizedType).run {
                require(this is ParameterizedType) { "For using this factory you have to wrap data model to ${Result::class.java}" }
                FlowCallAdapter<Any>(getParameterUpperBound(0, this))
            }
        } else {
            null
        }
}