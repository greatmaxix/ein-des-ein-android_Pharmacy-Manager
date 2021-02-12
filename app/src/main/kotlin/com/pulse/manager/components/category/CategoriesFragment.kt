package com.pulse.manager.components.category

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.category.adapter.CategoryAdapter
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class CategoriesFragment : BaseMVVMFragment(R.layout.fragment_categories) {

    private val args by navArgs<CategoriesFragmentArgs>()
    private val binding by viewBinding(FragmentCategoriesBinding::bind)
    private val viewModel: CategoriesViewModel by viewModel { parametersOf(args.category) }
    private val clickAction by lazy { return@lazy viewModel::selectCategory }
    private val categoryAdapter by lazy { CategoryAdapter(clickAction) }
    private val spacing by lazy { dimensionPixelSize(R.dimen._2sdp) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { viewModel.handleBackPress() }
        attachBackPressCallback { viewModel.handleBackPress() }
        viewSearch.onBackClick = { viewModel.handleBackPress() }
        initMenu(R.menu.search) {
            it.isVisible = false
            toolbar.toolbar.title = null
            viewSearch.animateVisibleIfNot()
            viewSearch.requestFocus()
            hideBackButton()
            true
        }
        initCategoryList()
        viewSearch.setSearchListener { value ->
            viewLifecycleOwner.lifecycleScope.launch { categoryAdapter.filter { it.name?.contains(value, true).falseIfNull() } }
        }
    }

    private fun initCategoryList() = with(binding.rvCategories) {
        adapter = categoryAdapter
        addItemDecorator(true, spacing)
        setHasFixedSize(true)
    }

    override fun navigationBack() {
        with(binding) {
            if (viewSearch.isVisible) {
                hideKeyboard()
                viewSearch.animateGoneIfNot()
                toolbar.toolbar.menu?.findItem(R.id.search)?.isVisible = true
                toolbar.toolbar.title = getString(R.string.menuTitleCategories)
                showBackButton()
            } else {
                navController.popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewSearch.setText("") // TODO review this case
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.navigateBackLiveData) { navigationBack() }
        observe(viewModel.parentCategoriesLiveData, categoryAdapter::notifyDataSet)
        observe(viewModel.nestedCategoriesLiveData, categoryAdapter::notifyDataSet)
    }
}