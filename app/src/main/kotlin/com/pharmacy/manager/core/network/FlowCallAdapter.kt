package com.pharmacy.manager.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.awaitResponse
import java.lang.reflect.Type

class FlowCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<Result<T>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<T>) = flow {
        emit(Result.create(call.awaitResponse()))
    }
}