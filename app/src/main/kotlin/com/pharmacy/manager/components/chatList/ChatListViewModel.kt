package com.pharmacy.manager.components.chatList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.chatList.repository.ChatListRemoteMediator
import com.pharmacy.manager.components.chatList.repository.ChatListRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.util.Constants.Companion.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ChatListViewModel(private val repository: ChatListRepository) : BaseViewModel() {

    private val filterTextLiveData by lazy { MutableLiveData<String>() }
    private val config = PagingConfig(PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 1, initialLoadSize = PAGE_SIZE / 2)

    @ExperimentalPagingApi
    val chatListLiveData by lazy {
        Transformations.switchMap(filterTextLiveData) {
            if (it.isBlank()) {
                Pager(
                    config = config,
                    remoteMediator = ChatListRemoteMediator(repository, errorHandler),
                    pagingSourceFactory = { repository.getChatsPagingSource() }
                )
            } else {
                Pager(
                    config = config,
                    pagingSourceFactory = { repository.searchChatsPagingSource(it) }
                )
            }.flow
                .cachedIn(viewModelScope)
                .asLiveData(Dispatchers.IO)
        }
    }

    init {
        searchChat("")
    }

    fun searchChat(query: String) {
        filterTextLiveData.postValue(query)
    }
}