package com.pulse.manager.components.category.repository

import com.pulse.manager.components.category.extra.flattenCategories

class CategoriesRepository(private val rds: CategoriesRemoteDataSource, private val lds: CategoriesLocalDataSource) {

    suspend fun categories() = if (lds.isCategoriesPresent()) {
        lds.categories()
    } else {
        rds.categories()
            .flattenCategories
            .also { lds.saveCategories(it) }
    }
}