package com.pharmacy.manager.components.category.repository

class CategoriesRepository(private val rds: CategoriesRemoteDataSource) {

    suspend fun categories() = rds.categories()
}