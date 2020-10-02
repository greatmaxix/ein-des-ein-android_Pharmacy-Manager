package com.pharmacy.manager.components.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.chat.repository.ChatMessagesRemoteMediator
import com.pharmacy.manager.components.chat.repository.ChatRepository
import com.pharmacy.manager.components.chatList.model.ChatItem
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.util.Constants
import timber.log.Timber
import java.io.File

class ChatViewModel(
    private val context: Context,
    private val repository: ChatRepository,
    args: ChatFragmentArgs
) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _chatLiveData by lazy { MutableLiveData<ChatItem>() }
    val chatLiveData: LiveData<ChatItem> by lazy { _chatLiveData }

    @ExperimentalPagingApi
    val chatMessagesLiveData by lazy {
        Pager(
            config = PagingConfig(Constants.PAGE_SIZE, initialLoadSize = Constants.INIT_LOAD_SIZE, enablePlaceholders = false),
            remoteMediator = ChatMessagesRemoteMediator(repository, errorHandler, args.chat.id),
            pagingSourceFactory = { repository.getMessagePagingSource(args.chat.id) }
        ).flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    val tempPhotoFile = File(context.externalCacheDir, Constants.TEMP_PHOTO_FILE_NAME)

    init {
        mockPharmacyMessages(true)
        _chatLiveData.value = args.chat
    }

    fun sendMessage(message: String) {
        addMessageToChatList(message)

        // TODO send message to server
    }


    fun sendProduct(product: Product) {
        // TODO send message to server

        addMessageToChatList(product = product)
    }

    fun sendPhotos(uriList: List<Uri>) {
        addMessageToChatList(images = uriList.map { it.toString() }.toMutableList())
        // TODO send photos
        Timber.e(uriList.toString())
    }

    private fun addMessageToChatList(message: String? = null, images: MutableList<String>? = null, product: Product? = null) {
        // TODO
//        val list = chatMessagesLiveData.value ?: arrayListOf()
//        if (list.isEmpty()) list.add(0, ChatMessage.DateHeader(LocalDateTime.now()))
//        if (!message.isNullOrBlank()) {
//            list.add(0, ChatMessage.PharmacyMessage(message))
//        } else if (!images.isNullOrEmpty()) {
//            list.add(0, ChatMessage.Attachment(images))
//        } else if (product != null) {
//            list.add(0, ChatMessage.ChatProduct(product))
//        }
//
//        _chatMessagesLiveData.value = list

        mockPharmacyMessages()
    }

    // TODO remove
    @Deprecated("Mock data method")
    private fun mockPharmacyMessages(startMessages: Boolean = false) {
        // TODO
//        val list = chatMessagesLiveData.value ?: arrayListOf()
//        if (startMessages) {
//            list.add(0, ChatMessage.DateHeader(LocalDateTime.now()))
//            list.add(0, ChatMessage.PharmacyMessage("Добрый день! Меня зовут Эстер"))
//            list.add(0, ChatMessage.PharmacyMessage("Чем я могу Вам помочь?"))
//            list.add(0, ChatMessage.UserMessage("Мне нужно что то от опухшего горла"))
//        } else {
//            list.add(0, ChatMessage.UserMessage(DummyData.userResponses.random()))
//        }
//
//        _chatMessagesLiveData.value = list
    }
}