package com.pharmacy.manager.components.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.chatList.repository.ChatListPagingSource
import com.pharmacy.manager.components.home.repository.HomeRepository
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.data.DummyData

class HomeViewModel(private val repository: HomeRepository) : BaseViewModel() {

    val chatListLiveData by lazy {
        Pager(PagingConfig(HOME_CHAT_LIST_SIZE, initialLoadSize = HOME_CHAT_LIST_SIZE)) { ChatListPagingSource() }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    private val _recentProductListLiveData by lazy { SingleLiveEvent<MutableList<Product>>() }
    val recentProductListLiveData: LiveData<MutableList<Product>> by lazy { _recentProductListLiveData }

    init {
        _recentProductListLiveData.value = DummyData.products.toMutableList()
    }

    companion object {

        private const val HOME_CHAT_LIST_SIZE = 2
    }
}