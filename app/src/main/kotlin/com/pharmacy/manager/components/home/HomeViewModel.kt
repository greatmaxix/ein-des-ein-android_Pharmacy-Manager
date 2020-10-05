package com.pharmacy.manager.components.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.chatList.repository.ChatListPagingSource
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.product.repository.ProductRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent

class HomeViewModel(private val repository: ProductRepository, private val productRepository: ProductRepository) : BaseViewModel() {

    val chatListLiveData by lazy {
        Pager(PagingConfig(HOME_CHAT_LIST_SIZE, initialLoadSize = HOME_CHAT_LIST_SIZE)) { ChatListPagingSource() }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    private val _recentProductListLiveData by lazy { SingleLiveEvent<List<ProductLite>>() }
    val recentProductListLiveData: LiveData<List<ProductLite>> by lazy { _recentProductListLiveData }

    fun getRecentProductList() = launchIO {
        _recentProductListLiveData.postValue(productRepository.getRecentlyRecommended())
    }

    fun requestProductInfo(globalProductId: Int) = requestLiveData {
        repository.productById(globalProductId)
    }

    companion object {

        private const val HOME_CHAT_LIST_SIZE = 2
    }
}