package com.pharmacy.manager.components.product.repository

import com.pharmacy.manager.components.product.model.Product

class ProductRepository(private val rds: ProductRemoteDataSource, private val lds: ProductLocalDataSource) {

    suspend fun productById(globalProductId: Int) = rds.getProductById(globalProductId)
        .dataOrThrow()
        .item

    suspend fun getRecentlyViewed() = lds.getRecentlyViewed()

    suspend fun saveRecentlyViewed(product: Product) { // TODO add only recommended, not viewed
        val lastProduct = getRecentlyViewed().firstOrNull()?.apply { primaryKey = 1 }
        if (lastProduct != product) {
            lds.save(listOfNotNull(product, lastProduct))
        }
    }
}