package com.pharmacy.manager.components.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.chat.repository.ChatMessagesRemoteMediator
import com.pharmacy.manager.components.chat.repository.ChatRepository
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.search.SearchViewModel
import com.pharmacy.manager.core.extensions.getMultipartBody
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.util.Constants
import com.pharmacy.manager.util.ImageFileUtil
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinApiExtension
import java.io.File

@KoinApiExtension
class ChatViewModel(
    private val context: Context,
    private val repository: ChatRepository,
    private val chat: ChatItem
) : SearchViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _chatLiveData by lazy { MutableLiveData<ChatItem>() }
    val chatLiveData: LiveData<ChatItem> by lazy { _chatLiveData }

    val lastMessageLiveData = repository.getLastMessageLiveData(chat.id)
        .distinctUntilChanged()

    @ExperimentalPagingApi
    val chatMessagesLiveData by lazy {
        Pager(
            config = PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 1, initialLoadSize = Constants.PAGE_SIZE / 2),
            remoteMediator = ChatMessagesRemoteMediator(repository, errorHandler, chat),
            pagingSourceFactory = { repository.getMessagePagingSource(chat.id) }
        ).flow
            .cachedIn(viewModelScope)
            .asLiveData(Dispatchers.IO)
    }

    val tempPhotoFile = File(context.externalCacheDir, Constants.TEMP_PHOTO_FILE_NAME)

    init {
        _chatLiveData.value = chat
    }

    fun sendMessage(message: String) = requestLiveData {
        repository.sendMessage(chat.id, message)
    }

    fun sendProduct(product: ProductLite) = requestLiveData {
        saveRecentlyRecommended(product)
        repository.sendProductMessage(chat.id, product.globalProductId)
    }

    fun sendPhoto(uri: Uri) = requestLiveData {
        ImageFileUtil.saveImageByUriToFile(context, tempPhotoFile, uri)
        ImageFileUtil.compressImage(context, tempPhotoFile, uri)

        val multipart = tempPhotoFile.getMultipartBody("file")
        val response = repository.uploadImage(multipart)
        response.uuid?.let {
            tempPhotoFile.delete()
            repository.sendImageMessage(chat.id, response.uuid)
        }
    }

    fun requestCloseChat() = requestLiveData {
        repository.requestCloseChat(chat.id)
    }
}