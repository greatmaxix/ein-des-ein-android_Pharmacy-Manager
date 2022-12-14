package com.pulse.manager.components.search.repository

import com.pulse.manager.data.rest.RestApi

class SearchRemoteDataSource(private val ra: RestApi) {

    suspend fun globalSearch(name: String? = null, page: Int? = null, pageSize: Int? = null, categoryCode: String? = null) =
        ra.productSearch(page, pageSize, name = name, categoryCode = categoryCode)
}