package com.pharmacy.manager.components.category

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.pharmacy.manager.R
import com.pharmacy.manager.components.category.adapter.CategoriesAdapter
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.coroutines.launch

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
            true
        }
        searchViewCategories.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                val adapter = rvCategories.adapter as CategoriesAdapter
                adapter.filter { it.name.contains(text, true).falseIfNull() }
            }
        }
    }

    override fun navigationBack() {
        if (searchViewCategories.isVisible) {
            hideKeyboard()
            searchViewCategories.animateGoneIfNot()
            toolbar?.menu?.findItem(R.id.search)?.isVisible = true
            toolbar?.title = viewModel.selectedCategoryLiveData.value?.name ?: getString(R.string.menuTitleCategories)
        } else {
            viewModel.handleBackPress()
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(this) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(this) }
        observe(viewModel.directionLiveData, navController::navigate)

        observe(viewModel.navigateBackLiveData) { navController.popBackStack() }
        observeNullable(viewModel.selectedCategoryLiveData) {
            if (searchViewCategories.isVisible) {
                toolbar?.title = null
            } else {
                toolbar?.title = this?.name ?: getString(R.string.menuTitleCategories)
            }
        }
        observe(viewModel.parentCategoriesLiveData) {
            rvCategories.adapter = CategoriesAdapter(this.toMutableList(), clickAction)
            rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
            clearItemDecoration()
            rvCategories.addGridItemDecorator()
        }
        observe(viewModel.nestedCategoriesLiveData) {
            rvCategories.adapter = CategoriesAdapter(this.toMutableList(), clickAction) // todo temp
            rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
            clearItemDecoration()
            rvCategories.addGridItemDecorator()
        }
    }

    private fun clearItemDecoration() {
        if (rvCategories.itemDecorationCount > 0) rvCategories.removeItemDecorationAt(0)
    }
}