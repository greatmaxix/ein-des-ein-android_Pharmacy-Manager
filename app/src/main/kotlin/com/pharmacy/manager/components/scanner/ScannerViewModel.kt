package com.pharmacy.manager.components.scanner

import androidx.lifecycle.LiveData
import com.pharmacy.manager.components.product.BaseProductViewModel
import com.pharmacy.manager.components.scanner.repository.ScannerRepository
import com.pharmacy.manager.core.general.SingleLiveEvent

class ScannerViewModel(private val repository: ScannerRepository) : BaseProductViewModel() {

    private val _descriptionVisibility by lazy { SingleLiveEvent<Boolean>() }
    val descriptionVisibility: LiveData<Boolean> by lazy { _descriptionVisibility }

    init {
        _descriptionVisibility.value = !repository.isQrCodeDescriptionShown()
    }

    fun descriptionViewed() {
        _descriptionVisibility.value = false
        repository.setDescriptionShown()
    }

    fun searchQrCode(code: String) = requestSingleLiveData {
        repository.searchBarcode(code)
    }
}