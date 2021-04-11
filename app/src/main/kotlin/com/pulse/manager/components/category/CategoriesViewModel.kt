package com.pulse.manager.components.category

import androidx.core.text.trimmedLength
import androidx.lifecycle.viewModelScope
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.manager.components.category.CategoriesFragmentDirections.Companion.fromCategoryToSearch
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.components.category.repository.CategoriesRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import com.pulse.manager.core.extensions.falseIfNull
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class CategoriesViewModel(private val repository: CategoriesRepository, private var selectedCategory: Category?) : BaseViewModel() {

    val parentCategoriesState = StateEventFlow<List<Category>?>(listOf())
    val nestedCategoriesState = StateEventFlow<List<Category>?>(null)
    val navigateBackEvent = SingleShotEvent<Unit>()

    private var originalList: List<Category>? = null
    private var parentList: List<Category>? = null

    init {
        viewModelScope.execute {
            originalList = repository.categories()
            parentList = originalList?.filter { it.code.trimmedLength() == 1 }
            selectedCategory?.let {
                selectCategory(it)
            } ?: run {
                parentCategoriesState.postState(parentList)
            }
        }
    }

    fun handleBackPress() {
        val code = selectedCategory?.code ?: run {
            navigateBackEvent.offerEvent(Unit)
            return
        }
        if (code.trimmedLength() == 1) {
            parentCategoriesState.postState(parentList)
            selectedCategory = null
        } else {
            nestedCategoriesState.postState(findParentCategories())
        }
    }

    fun selectCategory(category: Category) {
        category.apply {
            nodes = originalList?.filter { category.nestedCategories?.contains(it.code).falseIfNull() }
        }
        if (category.nodes?.isNotEmpty().falseIfNull()) {
            selectedCategory = category
            nestedCategoriesState.postState(category.nodes)
        } else {
            navEvent.postEvent(fromCategoryToSearch(category.code))
        }
    }

    private fun findParentCategories(): List<Category>? {
        val find = originalList?.find { it.nestedCategories?.contains(selectedCategory?.code).falseIfNull() }
        selectedCategory = find
        return originalList?.filter { find?.nestedCategories?.contains(it.code).falseIfNull() }
    }
}