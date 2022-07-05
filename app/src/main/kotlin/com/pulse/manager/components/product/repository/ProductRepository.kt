package com.pulse.manager.components.product.repository

import com.pulse.manager.components.product.model.ProductLite

class ProductRepository(private val rds: ProductRemoteDataSource, private val lds: ProductLocalDataSource) {

    suspend fun productById(globalProductId: Int) = rds.getProductById(globalProductId)
        .dataOrThrow()
        .item

    suspend fun getRecentlyRecommended() = lds.getRecentlyRecommended()

    suspend fun saveRecentlyRecommended(product: ProductLite) {
        val lastProduct = getRecentlyRecommended().firstOrNull()?.apply { primaryKey = 1 }
        if (lastProduct != product) {
            lds.save(listOfNotNull(product, lastProduct))
        }
    }
}