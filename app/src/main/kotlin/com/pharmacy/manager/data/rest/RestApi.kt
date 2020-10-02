package com.pharmacy.manager.data.rest

import androidx.annotation.WorkerThread
import com.pharmacy.manager.components.chat.model.SendMessageBody
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chatList.model.ChatItem
import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.data.rest.interceptor.HeaderInterceptor.Companion.HEADER_IGNORE_AUTHORIZATION
import com.pharmacy.manager.data.rest.request.LogInRequest
import com.pharmacy.manager.data.rest.request.LogOutRequest
import com.pharmacy.manager.data.rest.response.BaseDataResponse
import com.pharmacy.manager.data.rest.response.LogInResponse
import com.pharmacy.manager.data.rest.response.PaginationModel
import com.pharmacy.manager.data.rest.response.SingleItemModel
import retrofit2.http.*

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


    @WorkerThread
    @GET("${API_PATH}/chat/chats")
    suspend fun chatList(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<ChatItem>>

    @WorkerThread
    @GET("${API_PATH}/chat/chat/{chatId}/messages")
    suspend fun messagesList(
        @Path("chatId") chatId: Int,
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null
    ): BaseDataResponse<PaginationModel<MessageItem>>

    @WorkerThread
    @POST("${API_PATH}/chat/chat/{chatId}/message")
    suspend fun sendMessage(@Path("chatId") chatId: Int, @Body body: SendMessageBody): BaseDataResponse<SingleItemModel<MessageItem>>

    companion object {

        private const val API_PATH = "/api/v1"
    }
}