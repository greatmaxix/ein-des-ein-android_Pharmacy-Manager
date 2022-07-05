package com.pulse.manager.components.scanner.repository


class ScannerRepository(
    private val rds: ScannerRemoteDataSource,
    private val lds: ScannerLocalDataSource
) {

    fun isQrCodeDescriptionShown(): Boolean = lds.isQrCodeDescriptionShown()

    suspend fun setDescriptionShown() = lds.setDescriptionShown()

    suspend fun searchBarcode(code: String) = rds.searchBarcode(barCode = code)
        .dataOrThrow()
        .items
}