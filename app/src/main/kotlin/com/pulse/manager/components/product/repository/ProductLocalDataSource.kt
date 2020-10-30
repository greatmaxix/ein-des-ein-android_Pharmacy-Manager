package com.pulse.manager.components.product.repository

import androidx.room.Transaction
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.product.model.RecentlyRecommendedDAO

class ProductLocalDataSource(private val recentlyRecommended: RecentlyRecommendedDAO) {

    suspend fun getRecentlyRecommended() = recentlyRecommended.getList()

    @Transaction
    suspend fun save(products: List<ProductLite>) {
        recentlyRecommended.clear()
        recentlyRecommended.insert(products)
    }
}