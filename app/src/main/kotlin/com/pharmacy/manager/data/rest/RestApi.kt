package com.pharmacy.manager.data.rest

import androidx.annotation.WorkerThread
import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.data.rest.interceptor.HeaderInterceptor.Companion.HEADER_IGNORE_AUTHORIZATION
import com.pharmacy.manager.data.rest.request.LogInRequest
import com.pharmacy.manager.data.rest.request.LogOutRequest
import com.pharmacy.manager.data.rest.response.BaseDataResponse
import com.pharmacy.manager.data.rest.response.LogInResponse
import com.pharmacy.manager.data.rest.response.SingleItemModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RestApi {

    @WorkerThread
    @Headers(HEADER_IGNORE_AUTHORIZATION)
    @POST("${API_PATH}/user/login")
    suspend fun login(@Body body: LogInRequest): BaseDataResponse<LogInResponse>

    @WorkerThread
    @GET("${API_PATH}/user/user")
    suspend fun fetchUser(): BaseDataResponse<SingleItemModel<User>>

    @WorkerThread
    @POST("${API_PATH}/user/logout")
    suspend fun logout(@Body body: LogOutRequest): BaseDataResponse<Unit>

    companion object {

        private const val API_PATH = "/api/v1"
    }
}