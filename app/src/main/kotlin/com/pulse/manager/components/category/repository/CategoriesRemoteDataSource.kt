package com.pulse.manager.components.category.repository

import com.pulse.manager.data.rest.RestApi

class CategoriesRemoteDataSource(private val ra: RestApi) {

    suspend fun categories() = ra.categories().dataOrThrow().items
}