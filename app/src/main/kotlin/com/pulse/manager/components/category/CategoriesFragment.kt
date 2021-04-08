package com.pulse.manager.components.category

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.category.adapter.CategoryAdapter
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class CategoriesFragment : BaseToolbarFragment<CategoriesViewModel>(R.layout.fragment_categories, CategoriesViewModel::class, R.menu.search) {

    private val args by navArgs<CategoriesFragmentArgs>()
    private val binding by viewBinding(FragmentCategoriesBinding::bind)
    override val viewModel: CategoriesViewModel by viewModel { parametersOf(args.category) }
    private val clickAction by lazy { return@lazy viewModel::selectCategory }
    private val categoryAdapter by lazy { CategoryAdapter(clickAction) }
    private val spacing by lazy { dimensionPixelSize(R.dimen._2sdp) }

    override fun initUI() = with(binding) {
        showBackButton()
        attachBackPressCallback { viewModel.handleBackPress() }
        viewSearch.onBackClick = { viewModel.handleBackPress() }
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

    override fun onResume() {
        super.onResume()
        binding.viewSearch.setText("") // TODO review this case
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

    override fun onClickNavigation() {
        navigationBack()
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.navigateBackEvent.events) { onClickNavigation() }
        observe(menuItemsFlow) {
            it.isVisible = false
            binding.apply {
                toolbar.toolbar.title = null
                viewSearch.animateVisibleIfNot()
                viewSearch.requestFocus()
                hideBackButton()
            }
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.parentCategoriesState) { it?.let(categoryAdapter::notifyDataSet) }
        observe(viewModel.nestedCategoriesState) { it?.let(categoryAdapter::notifyDataSet) }
    }
}