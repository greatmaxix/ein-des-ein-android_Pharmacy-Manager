package com.pulse.manager.components.scanner

import androidx.lifecycle.viewModelScope
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.manager.components.product.BaseProductViewModel
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.scanner.repository.ScannerRepository
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ScannerViewModel(private val repository: ScannerRepository) : BaseProductViewModel() {

    val descriptionVisibilityState = StateEventFlow(!repository.isQrCodeDescriptionShown())
    val searchResultEvent = SingleShotEvent<List<ProductLite>>()

    fun descriptionViewed() = viewModelScope.execute {
        descriptionVisibilityState.postState(false)
        repository.setDescriptionShown()
    }

    fun searchQrCode(code: String) = viewModelScope.execute {
        searchResultEvent.sendEvent(repository.searchBarcode(code))
    }
}