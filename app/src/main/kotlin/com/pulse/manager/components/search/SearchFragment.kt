package com.pulse.manager.components.search

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.search.SearchFragmentDirections.Companion.fromSearchToScanner
import com.pulse.manager.core.extensions.observe
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.core.extensions.spanSearchCount
import com.pulse.manager.core.extensions.visibleOrGone
import com.pulse.manager.databinding.FragmentSearchBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension

@ExperimentalCoroutinesApi
@KoinApiExtension
class SearchFragment : BaseProductListFragment<SearchViewModel>(R.layout.fragment_search, SearchViewModel::class) {

    private val args by navArgs<SearchFragmentArgs>()
    private val binding by viewBinding(FragmentSearchBinding::bind)
    override val productFlow
        get() = viewModel.pagedSearchFlow

    override fun initUI() = with(binding) {
        super.initUI()

        viewModel.setSearchCategory(args.category)
        mcvScan.setDebounceOnClickListener { navController.navigate(fromSearchToScanner()) }
        viewSearch.setSearchListener { viewModel.doSearch(it.toString()) }
        viewSearch.onBackClick = { requireActivity().onBackPressed() }
    }

    override fun onBindStates() {
        super.onBindStates()
        with(lifecycleScope) {
            observe(viewModel.productCountState) {
                binding.mtvSearchResult.text = getString(R.string.countProducts, it).spanSearchCount(it)
                binding.llDrugsNotFoundContainer.visibleOrGone(it == 0)
            }
        }
    }
}