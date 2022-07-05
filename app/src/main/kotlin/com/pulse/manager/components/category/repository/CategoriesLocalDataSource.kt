package com.pulse.manager.components.category.repository

import com.pulse.manager.components.category.model.Category
import com.pulse.manager.components.category.model.CategoryDAO

class CategoriesLocalDataSource(private val dao: CategoryDAO) {

    suspend fun saveCategories(categories: List<Category>) = dao.insert(categories)

    suspend fun categories() = dao.categories()

    suspend fun isCategoriesPresent() = dao.categoriesSize() > 0
}