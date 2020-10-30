package com.pulse.manager.components.product

import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.product.repository.ProductRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
abstract class BaseProductViewModel : BaseViewModel(), KoinComponent {

    private val repositoryProduct by inject<ProductRepository>()

    fun requestProductInfo(globalProductId: Int) = requestLiveData {
        repositoryProduct.productById(globalProductId)
    }

    fun saveRecentlyRecommended(product: ProductLite) {
        launchIO {
            repositoryProduct.saveRecentlyRecommended(product)
        }
    }

    companion object {

        const val PAGE_SIZE = 20
        const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}