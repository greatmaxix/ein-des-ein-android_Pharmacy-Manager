package com.pharmacy.manager.components.scanner

import androidx.lifecycle.LiveData
import com.pharmacy.manager.components.product.BaseProductViewModel
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.scanner.repository.ScannerRepository
import com.pharmacy.manager.core.general.SingleLiveEvent

class ScannerViewModel(private val repository: ScannerRepository) : BaseProductViewModel() {

    private val _resultLiveData by lazy { SingleLiveEvent<List<ProductLite>>() }
    val resultLiveData: LiveData<List<ProductLite>> by lazy { _resultLiveData }

    private val _descriptionVisibility by lazy { SingleLiveEvent<Boolean>() }
    val descriptionVisibility: LiveData<Boolean> by lazy { _descriptionVisibility }

    init {
        _descriptionVisibility.value = !repository.isQrCodeDescriptionShown()
    }

    fun descriptionViewed() {
        _descriptionVisibility.value = false
        repository.setDescriptionShown()
    }

    fun searchQrCode(code: String) {
        _progressLiveData.postValue(true)
        launchIO {
            // TODO
//            when (val response = repository.searchQrCode(code)) {
//                is ResponseWrapper.Success -> {
//                    val items = response.value.data.items
//                    if (items.size == 1) {
//                        getProductInfo(items.first().globalProductId)
//                    } else {
//                        _resultLiveData.postValue(items)
//                    }
//                }
//                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
//            }
            _progressLiveData.postValue(false)
        }
    }
}