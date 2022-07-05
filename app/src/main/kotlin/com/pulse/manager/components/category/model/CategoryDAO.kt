package com.pulse.manager.components.category.model

import androidx.room.Dao
import androidx.room.Query
import com.pulse.manager.data.local.db.BaseDao

@Dao
interface CategoryDAO : BaseDao<Category> {

    @Query("select * from category")
    suspend fun categories(): List<Category>

    @Query("SELECT COUNT(*) FROM category")
    suspend fun categoriesSize(): Int

    @Query("select * from category limit 1")
    fun get(): Category?

    @Query("DELETE FROM category")
    fun clear()
}