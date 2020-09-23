package com.pharmacy.manager.components.category

import androidx.core.text.trimmedLength
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pharmacy.manager.components.category.model.Category
import com.pharmacy.manager.components.category.repository.CategoriesRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.data.DummyData

class CategoriesViewModel(private val repository: CategoriesRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _parentCategoriesLiveData by lazy { SingleLiveEvent<List<Category>>() }
    val parentCategoriesLiveData: LiveData<List<Category>> by lazy { _parentCategoriesLiveData }

    private val _nestedCategoriesLiveData by lazy { SingleLiveEvent<List<Category>>() }
    val nestedCategoriesLiveData: LiveData<List<Category>> by lazy { _nestedCategoriesLiveData }

    private val _navigateBackLiveData by lazy { MutableLiveData<Unit>() }
    val navigateBackLiveData: LiveData<Unit> by lazy { _navigateBackLiveData }

    private val _selectedCategoryLiveData by lazy { MutableLiveData<Category?>() }
    val selectedCategoryLiveData: LiveData<Category?> by lazy { _selectedCategoryLiveData }

    private var originalList: List<Category>? = null

    init {
        // TODO
        // _progressLiveData.value = true
        launchIO {
//            val response = repository.getCategories()
//            _progressLiveData.postValue(false)
//            when (response) {
//                is Success -> {
//                    _parentCategoriesLiveData.postValue(response.value.data.items)
            originalList = DummyData.categories
            _parentCategoriesLiveData.postValue(originalList)
//                    originalList = response.value.data.items
//                }
//                is Error -> _errorLiveData.postValue(response.errorMessage)
//            }
        }
    }

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
            // todo go to search
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