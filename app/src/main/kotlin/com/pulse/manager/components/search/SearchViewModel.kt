package com.pulse.manager.components.search

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.manager.components.product.BaseProductViewModel
import com.pulse.manager.components.search.repository.SearchPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalCoroutinesApi
open class SearchViewModel : BaseProductViewModel() {

    private val searchState = StateEventFlow("")
    private var searchCategoryCode: String? = null

    val productCountState = StateEventFlow(0)

    val pagedSearchFlow by lazy {
        searchState.flatMapLatest {
            Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { SearchPagingSource(it, searchCategoryCode, productCountState::postState) }
                .flow
                .cachedIn(viewModelScope)
        }
    }

    fun doSearch(value: String) = searchState.postState(value)

    fun setSearchCategory(code: String?) {
        searchCategoryCode = code
    }
}