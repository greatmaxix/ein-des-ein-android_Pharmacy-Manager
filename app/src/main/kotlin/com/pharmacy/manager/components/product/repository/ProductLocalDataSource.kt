package com.pharmacy.manager.components.product.repository

import androidx.room.Transaction
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.components.product.model.RecentlyRecommendedDAO

class ProductLocalDataSource(private val recentlyRecommended: RecentlyRecommendedDAO) {

    suspend fun getRecentlyViewed() = recentlyRecommended.getList()

    @Transaction
    suspend fun save(products: List<Product>) {
        recentlyRecommended.clear()
        recentlyRecommended.insert(products)
    }
}