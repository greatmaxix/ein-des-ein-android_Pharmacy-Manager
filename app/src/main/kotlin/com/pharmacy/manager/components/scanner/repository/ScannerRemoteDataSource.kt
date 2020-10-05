package com.pharmacy.manager.components.scanner.repository

import com.pharmacy.manager.data.rest.RestApi

class ScannerRemoteDataSource(private val ra: RestApi) {

    suspend fun searchBarcode(barCode: String) = ra.productSearch(PAGE, PAGE_SIZE, barCode = barCode)

    companion object {
        private const val PAGE = 1
        private const val PAGE_SIZE = 20
    }
}