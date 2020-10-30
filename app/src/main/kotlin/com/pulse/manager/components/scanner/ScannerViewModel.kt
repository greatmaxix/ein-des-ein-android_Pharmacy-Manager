package com.pulse.manager.components.scanner

import androidx.lifecycle.LiveData
import com.pulse.manager.components.product.BaseProductViewModel
import com.pulse.manager.components.scanner.repository.ScannerRepository
import com.pulse.manager.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
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