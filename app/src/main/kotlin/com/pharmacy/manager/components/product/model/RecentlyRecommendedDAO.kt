package com.pharmacy.manager.components.product.model

import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.manager.data.local.db.BaseDao

@Dao
interface RecentlyRecommendedDAO : BaseDao<Product> {

    @Query("SELECT * FROM Product LIMIT 2")
    suspend fun getList(): List<Product>

    @Query("DELETE FROM Product")
    fun clear()
}