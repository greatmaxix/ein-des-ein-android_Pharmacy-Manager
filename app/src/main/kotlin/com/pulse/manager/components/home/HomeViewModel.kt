package com.pulse.manager.components.home

import androidx.lifecycle.viewModelScope
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.components.home.repository.HomeRepository
import com.pulse.manager.components.product.model.Product
import com.pulse.manager.components.product.repository.ProductRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import com.pulse.manager.data.rest.response.PaginationModel
import kotlinx.coroutines.flow.collect
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeViewModel(private val repository: HomeRepository, private val productRepository: ProductRepository) : BaseViewModel() {

    val openedChats = StateEventFlow<PaginationModel<ChatItem>?>(null)

    val recentProductListState = StateEventFlow<List<Product>>(listOf())

    init {
        viewModelScope.execute {
            repository.getOpenedChatsFlow().collect {
                openedChats.postState(repository.lastOpenedChats())
            }
        }
    }

    fun fetchLastRecommended() {
        viewModelScope.execute {
            recentProductListState.postState(repository.lastRecommendedProducts())
        }
    }

    fun requestProductInfo(globalProductId: Int) = viewModelScope.execute {
        val response = productRepository.productById(globalProductId)
        navEvent.postEvent(HomeFragmentDirections.globalToProductCard(response))
    }
}