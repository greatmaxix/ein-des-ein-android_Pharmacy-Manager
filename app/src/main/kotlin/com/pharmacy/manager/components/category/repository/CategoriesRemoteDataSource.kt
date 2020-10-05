package com.pharmacy.manager.components.category.repository

import com.pharmacy.manager.data.rest.RestApi

class CategoriesRemoteDataSource(private val ra: RestApi) {

    suspend fun categories() = ra.categories().dataOrThrow().items
}