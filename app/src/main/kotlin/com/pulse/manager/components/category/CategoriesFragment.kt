package com.pulse.manager.components.category

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pulse.manager.R
import com.pulse.manager.components.category.adapter.CategoriesAdapter
import com.pulse.manager.components.category.adapter.NestedCategoriesAdapter
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class CategoriesFragment : BaseMVVMFragment(R.layout.fragment_categories) {

    private val args by navArgs<CategoriesFragmentArgs>()
    private val viewModel: CategoriesViewModel by viewModel { parametersOf(args.category) }

    private val clickAction by lazy { return@lazy viewModel::selectCategory }
    private var adapter: BaseFilterRecyclerAdapter<Category, *>? = null
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen._4sdp) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { viewModel.handleBackPress() }
        attachBackPressCallback { viewModel.handleBackPress() }
        searchViewCategories.onBackClick = { viewModel.handleBackPress() }
        initMenu(R.menu.search) {
            it.isVisible = false
            toolbar?.title = null
            searchViewCategories.animateVisibleIfNot()
            searchViewCategories.requestFocus()
            hideBackButton()
            true
        }
        searchViewCategories.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch { adapter?.filter { it.name?.contains(text, true).falseIfNull() } }
        }
    }

    override fun navigationBack() {
        if (searchViewCategories.isVisible) {
            hideKeyboard()
            searchViewCategories.animateGoneIfNot()
            toolbar?.menu?.findItem(R.id.search)?.isVisible = true
            toolbar?.title = getString(R.string.menuTitleCategories)
            showBackButton()
        } else {
            navController.popBackStack()
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.navigateBackLiveData) { navigationBack() }
        observe(viewModel.parentCategoriesLiveData) {
            setAdapter(CategoriesAdapter(clickAction).apply { notifyDataSet(toMutableList()) })
            rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
            rvCategories.addGridItemDecorator()
        }
        observe(viewModel.nestedCategoriesLiveData) {
            setAdapter(NestedCategoriesAdapter(clickAction).apply { notifyDataSet(toMutableList()) })
            rvCategories.layoutManager = LinearLayoutManager(requireContext())
            rvCategories.addItemDecorator(true, spacing, spacing, spacing, spacing)
        }
    }

    private fun setAdapter(categoriesAdapter: BaseFilterRecyclerAdapter<Category, *>) {
        adapter = categoriesAdapter
        rvCategories.adapter = categoriesAdapter
        clearItemDecoration()
    }

    private fun clearItemDecoration() {
        if (rvCategories.itemDecorationCount > 0) rvCategories.removeItemDecorationAt(0)
    }
}