package com.pharmacy.manager.components.chatList

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.chatList.repository.ChatListPagingSource
import com.pharmacy.manager.components.chatList.repository.ChatListRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.util.Constants.Companion.INIT_LOAD_SIZE
import com.pharmacy.manager.util.Constants.Companion.PAGE_SIZE

class ChatListViewModel(private val repository: ChatListRepository) : BaseViewModel() {

    val chatListLiveData by lazy {
        Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { ChatListPagingSource() }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }
}