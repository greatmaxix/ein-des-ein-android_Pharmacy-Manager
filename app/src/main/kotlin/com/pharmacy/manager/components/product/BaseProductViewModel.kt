package com.pharmacy.manager.components.product

import com.pharmacy.manager.components.product.repository.ProductRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseProductViewModel : BaseViewModel(), KoinComponent {

    private val repositoryProduct by inject<ProductRepository>()

    fun requestProductInfo(globalProductId: Int) = requestLiveData {
        repositoryProduct.productById(globalProductId).apply {
            repositoryProduct.saveRecentlyViewed(this)
        }
    }

    companion object {

        const val PAGE_SIZE = 20
        const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}