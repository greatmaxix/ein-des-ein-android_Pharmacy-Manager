package com.pharmacy.manager.components.search.repository

import androidx.paging.PagingSource
import com.pharmacy.manager.components.product.model.ProductLite
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchPagingSource(private val text: String? = null, private val category: String? = null, private val total: (Int) -> Unit) : PagingSource<Int, ProductLite>(),
    KoinComponent {

    private val repository: SearchRepository by inject()

    override suspend fun load(params: LoadParams<Int>) = try {
        val response = repository.searchPaging(text, params.key ?: 1, params.loadSize, category)
        total(response.data.totalCount)
        LoadResult.Page(response.data.items, null, response.data.currentPageNumber + 1)
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}