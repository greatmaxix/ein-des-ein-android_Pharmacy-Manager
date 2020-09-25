package com.pharmacy.manager.components.home

import androidx.lifecycle.LiveData
import com.pharmacy.manager.components.chatList.model.TempChat
import com.pharmacy.manager.components.home.repository.HomeRepository
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.data.DummyData

class HomeViewModel(private val repository: HomeRepository) : BaseViewModel() {

    private val _chatListLiveData by lazy { SingleLiveEvent<ArrayList<TempChat>>() }
    val chatListLiveData: LiveData<ArrayList<TempChat>> by lazy { _chatListLiveData }

    private val _recentProductListLiveData by lazy { SingleLiveEvent<MutableList<Product>>() }
    val recentProductListLiveData: LiveData<MutableList<Product>> by lazy { _recentProductListLiveData }

    init {
        _chatListLiveData.value = DummyData.chatList
        _recentProductListLiveData.value = DummyData.products.toMutableList()
    }
}