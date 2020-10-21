package com.pharmacy.manager.data.rest

import androidx.annotation.WorkerThread
import com.pharmacy.manager.components.category.model.Category
import com.pharmacy.manager.components.chat.model.SendMessageBody
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chatList.model.AvatarShort
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.data.rest.interceptor.HeaderInterceptor.Companion.HEADER_IGNORE_AUTHORIZATION
import com.pharmacy.manager.data.rest.request.LogInRequest
import com.pharmacy.manager.data.rest.request.LogOutRequest
import com.pharmacy.manager.data.rest.response.*
import okhttp3.MultipartBody
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
        @Query("per_page") pageSize: Int? = null,
        @Query("all") all: Boolean = false,
        @Query("order") order: String? = "desc"
    ): BaseDataResponse<PaginationModel<ChatItem>>

    @WorkerThread
    @GET("${API_PATH}/chat/chat/{chatId}/messages")
    suspend fun messagesList(
        @Path("chatId") chatId: Int,
        @Query("per_page") pageSize: Int? = null,
        @Query("afterMessageNumber") afterMessageNumber: Int? = null,
        @Query("beforeMessageNumber") beforeMessageNumber: Int? = null
    ): BaseDataResponse<PaginationModel<MessageItem>>

    @WorkerThread
    @POST("${API_PATH}/chat/chat/{chatId}/message")
    suspend fun sendMessage(@Path("chatId") chatId: Int, @Body body: SendMessageBody): BaseDataResponse<SingleItemModel<MessageItem>>

    @WorkerThread
    @POST("${API_PATH}/chat/chat/{chatId}/global-product/{product_id}")
    suspend fun sendProductMessage(@Path("chatId") chatId: Int, @Path("product_id") globalProductId: Int): BaseDataResponse<SingleItemModel<MessageItem>>

    @WorkerThread
    @POST("${API_PATH}/chat/chat/{chatId}/application/{imageUuid}")
    suspend fun sendImageMessage(@Path("chatId") chatId: Int, @Path("imageUuid") imageUuid: String): BaseDataResponse<SingleItemModel<MessageItem>>

    @WorkerThread
    @GET("${API_PATH}/public/products/search")
    suspend fun productSearch(
        @Query("page") page: Int? = null,
        @Query("per_page") pageSize: Int? = null,
        @Query("regionId") regionId: Int? = null,
        @Query("barCode") barCode: String? = null,
        @Query("categoryCode") categoryCode: String? = null,
        @Query("name") name: String? = null
    ): BaseDataResponse<PaginationModel<ProductLite>>

    @WorkerThread
    @GET("${API_PATH}/public/products/global-product/{id}")
    suspend fun getProductById(@Path("id") globalProductId: Int): BaseDataResponse<SingleItemModel<Product>>

    @WorkerThread
    @GET("/api/v1/public/categories")
    suspend fun categories(): BaseDataResponse<ListItemsModel<Category>>

    @WorkerThread
    @Multipart
    @POST("${API_PATH}/user/image")
    suspend fun uploadImage(@Part file: MultipartBody.Part): BaseDataResponse<SingleItemModel<AvatarShort>>

    @PATCH("${API_PATH}/user/chat/{chatId}/close")
    suspend fun closeChat(@Path("chatId") chatId: Int): BaseDataResponse<SingleItemModel<ChatItem>>

    @GET("${API_PATH}/chat/chat/{chatId}")
    suspend fun getChat(@Path("chatId") chatId: Int): BaseDataResponse<SingleItemModel<ChatItem>>

    companion object {

        private const val API_PATH = "/api/v1"
    }
}