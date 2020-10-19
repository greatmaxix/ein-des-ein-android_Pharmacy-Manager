package com.pharmacy.manager.components.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.manager.components.product.BaseProductViewModel
import com.pharmacy.manager.components.search.repository.SearchPagingSource
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
open class SearchViewModel : BaseProductViewModel() {

    private val searchLiveData by lazy { MutableLiveData("") }
    private var searchCategoryCode: String? = null

    private val _productCountLiveData by lazy { MutableLiveData<Int>() }
    val productCountLiveData: LiveData<Int> by lazy { _productCountLiveData.distinctUntilChanged() }

    val pagedSearchLiveData by lazy {
        searchLiveData.distinctUntilChanged().switchMap {
            Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { SearchPagingSource(it, searchCategoryCode, _productCountLiveData::postValue) }
                .flow
                .cachedIn(viewModelScope)
                .asLiveData()
        }
    }

    fun doSearch(value: String) {
        searchLiveData.value = value
    }

    fun setSearchCategory(code: String?) {
        searchCategoryCode = code
    }
}