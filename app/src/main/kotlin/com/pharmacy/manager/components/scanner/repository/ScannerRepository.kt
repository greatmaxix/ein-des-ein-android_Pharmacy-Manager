package com.pharmacy.manager.components.scanner.repository


class ScannerRepository(
    private val rds: ScannerRemoteDataSource,
    private val lds: ScannerLocalDataSource
) {

    fun isQrCodeDescriptionShown(): Boolean = lds.isQrCodeDescriptionShown()

    fun setDescriptionShown() {
        lds.setDescriptionShown()
    }

//    suspend fun searchQrCode(code: String) = rds.searchBarcode(barCode = code) TODO
}