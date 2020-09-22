package com.pharmacy.manager.components.product

import androidx.lifecycle.LiveData
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import org.koin.core.KoinComponent

abstract class BaseProductViewModel : BaseViewModel(), KoinComponent {

    // TODO
//    private val repositoryUser by inject<UserRepository>()
//    private val repositoryProduct by inject<ProductRepository>()

    protected val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    protected val _errorLiveData by lazy { SingleLiveEvent<Int>() }
    val errorLiveData: LiveData<Int> by lazy { _errorLiveData }

    private val _productLiveData by lazy { SingleLiveEvent<Product>() }
    val productLiteLiveData: LiveData<Product> by lazy { _productLiveData }

    fun getProductInfo(globalProductId: Int) {
        launchIO {
            _progressLiveData.postValue(true)
            // TODO
//            when (val response = repositoryProduct.productById(globalProductId)) {
//                is ResponseWrapper.Success -> _productLiveData.postValue(response.value.data.item)
//                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
//            }
            _progressLiveData.postValue(false)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}