package com.pulse.manager.components.home

import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import com.pulse.manager.components.home.repository.HomeRepository
import com.pulse.manager.components.product.repository.ProductRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeViewModel(private val repository: HomeRepository, private val productRepository: ProductRepository) : BaseViewModel() {

    val openedChats
        get() = repository.getOpenedChatsLiveData()
            .distinctUntilChanged()
            .switchMap {
                requestLiveData { repository.lastOpenedChats() }
            }
    val recentProductListLiveData
        get() = requestLiveData { repository.lastRecommendedProducts() }

    fun requestProductInfo(globalProductId: Int) = requestLiveData {
        productRepository.productById(globalProductId)
    }
}