package com.pharmacy.manager.components.scanner.repository

import com.pharmacy.manager.data.local.SPManager

class ScannerLocalDataSource(private val sp: SPManager) {

    fun isQrCodeDescriptionShown(): Boolean = sp.qrCodeDescriptionShown ?: false

    fun setDescriptionShown() {
        sp.qrCodeDescriptionShown = true
    }
}