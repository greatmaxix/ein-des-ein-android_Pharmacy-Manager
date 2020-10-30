package com.pulse.manager.components.product.model

import androidx.room.Dao
import androidx.room.Query
import com.pulse.manager.data.local.db.BaseDao

@Dao
interface RecentlyRecommendedDAO : BaseDao<ProductLite> {

    @Query("SELECT * FROM ProductLite LIMIT 2")
    suspend fun getList(): List<ProductLite>

    @Query("DELETE FROM ProductLite")
    fun clear()
}