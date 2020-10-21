package com.pharmacy.manager.components.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.chatList.repository.ChatListRemoteMediator
import com.pharmacy.manager.components.chatList.repository.ChatListRepository
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.product.repository.ProductRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.util.Constants
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeViewModel(private val repository: ProductRepository, private val productRepository: ProductRepository, chatListRepository: ChatListRepository) : BaseViewModel() {

    @ExperimentalPagingApi
    val chatListLiveData by lazy {
        Pager(
            config = PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 1, initialLoadSize = Constants.PAGE_SIZE / 2),
            remoteMediator = ChatListRemoteMediator(chatListRepository, errorHandler),
            pagingSourceFactory = { chatListRepository.getChatsPagingSource() }
        ).flow
            .cachedIn(viewModelScope)
            .asLiveData(Dispatchers.IO)
    }

    private val _recentProductListLiveData by lazy { SingleLiveEvent<List<ProductLite>>() }
    val recentProductListLiveData: LiveData<List<ProductLite>> by lazy { _recentProductListLiveData }

    fun getRecentProductList() = launchIO {
        _recentProductListLiveData.postValue(productRepository.getRecentlyRecommended())
    }

    fun requestProductInfo(globalProductId: Int) = requestLiveData {
        repository.productById(globalProductId)
    }
}