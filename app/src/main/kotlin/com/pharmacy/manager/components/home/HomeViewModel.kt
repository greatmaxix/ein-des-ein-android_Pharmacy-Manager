package com.pharmacy.manager.components.home

import androidx.lifecycle.LiveData
import com.pharmacy.manager.components.chatList.model.TempChat
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.product.repository.ProductRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.data.DummyData

class HomeViewModel(private val repository: ProductRepository, private val productRepository: ProductRepository) : BaseViewModel() {

    private val _chatListLiveData by lazy { SingleLiveEvent<ArrayList<TempChat>>() }
    val chatListLiveData: LiveData<ArrayList<TempChat>> by lazy { _chatListLiveData }

    private val _recentProductListLiveData by lazy { SingleLiveEvent<List<ProductLite>>() }
    val recentProductListLiveData: LiveData<List<ProductLite>> by lazy { _recentProductListLiveData }

    init {
        _chatListLiveData.value = DummyData.chatList
    }

    fun getRecentProductList() = launchIO {
        _recentProductListLiveData.postValue(productRepository.getRecentlyRecommended())
    }

    fun requestProductInfo(globalProductId: Int) = requestLiveData {
        repository.productById(globalProductId)
    }
}