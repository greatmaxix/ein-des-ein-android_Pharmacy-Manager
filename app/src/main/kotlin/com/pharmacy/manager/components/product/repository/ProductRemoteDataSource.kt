package com.pharmacy.manager.components.product.repository

import com.pharmacy.manager.data.rest.RestApi

class ProductRemoteDataSource(private val ra: RestApi) {

    suspend fun getProductById(globalProductId: Int) = ra.getProductById(globalProductId)
}