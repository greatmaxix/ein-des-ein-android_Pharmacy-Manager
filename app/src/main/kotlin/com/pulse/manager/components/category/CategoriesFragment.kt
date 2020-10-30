package com.pulse.manager.components.category

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.pulse.manager.R
import com.pulse.manager.components.category.adapter.CategoriesAdapter
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class CategoriesFragment(private val viewModel: CategoriesViewModel) : BaseMVVMFragment(R.layout.fragment_categories) {

    private val clickAction = viewModel::adapterClicked

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initMenu(R.menu.search) {
            it.isVisible = false
            toolbar?.title = null
            searchViewCategories.animateVisibleIfNot()
            searchViewCategories.requestFocus()
            hideBackButton()
            true
        }
        searchViewCategories.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (rvCategories.adapter == null) return@launch
                val adapter = rvCategories.adapter as CategoriesAdapter
                adapter.filter { it.name.contains(text, true).falseIfNull() }
            }
        }
        searchViewCategories.onBackClick = {
            navigationBack()
        }
        observeResult(viewModel.baseCategoriesLiveData)
    }

    override fun navigationBack() {
        if (searchViewCategories.isVisible) {
            hideKeyboard()
            searchViewCategories.animateGoneIfNot()
            toolbar?.menu?.findItem(R.id.search)?.isVisible = true
            toolbar?.title = viewModel.selectedCategoryLiveData.value?.name ?: getString(R.string.menuTitleCategories)
            showBackButton()
        } else {
            viewModel.handleBackPress()
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.navigateBackLiveData) { navController.popBackStack() }
        observeNullable(viewModel.selectedCategoryLiveData) {
            if (searchViewCategories.isVisible) {
                toolbar?.title = null
            } else {
                toolbar?.title = this?.name ?: getString(R.string.menuTitleCategories)
            }
        }
        observe(viewModel.parentCategoriesLiveData, ::setAdapter)
        observe(viewModel.nestedCategoriesLiveData, ::setAdapter)
    }

    private fun setAdapter(list: List<Category>) {
        rvCategories.adapter = CategoriesAdapter(list.toMutableList(), clickAction)
        clearItemDecoration()
        rvCategories.addGridItemDecorator()
    }

    private fun clearItemDecoration() {
        if (rvCategories.itemDecorationCount > 0) rvCategories.removeItemDecorationAt(0)
    }
}