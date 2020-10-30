package com.pharmacy.manager.components.product.repository

import androidx.room.Transaction
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.product.model.RecentlyRecommendedDAO

class ProductLocalDataSource(private val recentlyRecommended: RecentlyRecommendedDAO) {

    suspend fun getRecentlyRecommended() = recentlyRecommended.getList()

    @Transaction
    suspend fun save(products: List<ProductLite>) {
        recentlyRecommended.clear()
        recentlyRecommended.insert(products)
    }
}