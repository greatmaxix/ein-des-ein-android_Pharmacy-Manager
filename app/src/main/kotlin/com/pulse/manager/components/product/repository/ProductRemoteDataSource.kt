package com.pulse.manager.components.product.repository

import com.pulse.manager.data.rest.RestApi

class ProductRemoteDataSource(private val ra: RestApi) {

    suspend fun getProductById(globalProductId: Int) = ra.getProductById(globalProductId)
}