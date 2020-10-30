package com.pulse.manager.components.category

import androidx.core.text.trimmedLength
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pulse.manager.components.category.CategoriesFragmentDirections.Companion.fromCategoryToSearch
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.components.category.repository.CategoriesRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import com.pulse.manager.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class CategoriesViewModel(private val repository: CategoriesRepository) : BaseViewModel() {

    val baseCategoriesLiveData = requestLiveData {
        repository.categories().apply {
            originalList = this
            _parentCategoriesLiveData.postValue(originalList)
        }
    }

    private val _parentCategoriesLiveData by lazy { MutableLiveData<List<Category>>() }
    val parentCategoriesLiveData: LiveData<List<Category>> by lazy { _parentCategoriesLiveData }

    private val _nestedCategoriesLiveData by lazy { MutableLiveData<List<Category>>() }
    val nestedCategoriesLiveData: LiveData<List<Category>> by lazy { _nestedCategoriesLiveData }

    private val _navigateBackLiveData by lazy { MutableLiveData<Unit>() }
    val navigateBackLiveData: LiveData<Unit> by lazy { _navigateBackLiveData }

    private val _selectedCategoryLiveData by lazy { MutableLiveData<Category?>() }
    val selectedCategoryLiveData: LiveData<Category?> by lazy { _selectedCategoryLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private var originalList: List<Category>? = null

    fun handleBackPress() {
        val code = selectedCategoryLiveData.value?.code ?: run {
            _navigateBackLiveData.postValue(Unit)
            return
        }
        if (code.trimmedLength() == 1) {
            _parentCategoriesLiveData.postValue(originalList)
            _selectedCategoryLiveData.postValue(null)
        } else {
            _nestedCategoriesLiveData.postValue(findParentCategories(originalList, code))
        }
    }

    fun adapterClicked(category: Category) {
        if (category.nodes.isNotEmpty()) {
            _selectedCategoryLiveData.postValue(category)
            _nestedCategoriesLiveData.postValue(category.nodes)
        } else {
            _directionLiveData.postValue(fromCategoryToSearch(category.code))
        }
    }

    private fun findParentCategories(list: List<Category>?, code: String): List<Category>? {
        return list?.find { code.startsWith(it.code) }?.run {
            return nodes.find { it.code == code }?.let {
                _selectedCategoryLiveData.postValue(this)
                return this.nodes
            } ?: run { findParentCategories(nodes, code) }
        }
    }
}