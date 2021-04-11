package com.pulse.manager.components.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.manager.components.chat.repository.ChatMessagesRemoteMediator
import com.pulse.manager.components.chat.repository.ChatRepository
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.search.SearchViewModel
import com.pulse.manager.core.extensions.getMultipartBody
import com.pulse.manager.util.Constants
import com.pulse.manager.util.ImageFileUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension
import java.io.File

@ExperimentalCoroutinesApi
@KoinApiExtension
class ChatViewModel(
    private val context: Context,
    private val repository: ChatRepository,
    private val chat: ChatItem
) : SearchViewModel() {

    val chatFlow = repository.getChatFlow(chat.id)
    val lastMessageFlow = repository.getLastMessageFlow(chat.id)

    @ExperimentalPagingApi
    val chatMessagesFlow by lazy {
        Pager(
            config = PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ChatMessagesRemoteMediator(repository, errorHandler, chat),
            pagingSourceFactory = { repository.getMessagePagingSource(chat.id) }
        )
            .flow
            .cachedIn(viewModelScope)
    }

    val tempPhotoFile = File(context.externalCacheDir, Constants.TEMP_PHOTO_FILE_NAME)

    fun sendMessage(message: String) = viewModelScope.execute {
        repository.sendMessage(chat.id, message)
    }

    fun sendProduct(product: ProductLite) = viewModelScope.execute {
        saveRecentlyRecommended(product)
        repository.sendProductMessage(chat.id, product.globalProductId)
    }

    fun sendPhoto(uri: Uri) = viewModelScope.execute {
        ImageFileUtil.saveImageByUriToFile(context, tempPhotoFile, uri)
        ImageFileUtil.compressImage(context, tempPhotoFile, uri)

        val multipart = tempPhotoFile.getMultipartBody("file")
        val response = repository.uploadImage(multipart)
        response.uuid?.let {
            tempPhotoFile.delete()
            repository.sendImageMessage(chat.id, response.uuid)
        }
    }

    fun requestCloseChat() = viewModelScope.execute {
        repository.requestCloseChat(chat.id)
    }
}