package com.pulse.manager.components.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.search.SearchFragmentDirections.Companion.fromSearchToScanner
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.core.extensions.spanSearchCount
import com.pulse.manager.core.extensions.visibleOrGone
import com.pulse.manager.databinding.FragmentSearchBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SearchFragment(private val viewModel: SearchViewModel) : BaseProductListFragment<SearchViewModel>(R.layout.fragment_search, viewModel) {

    private val args by navArgs<SearchFragmentArgs>()
    private val binding by viewBinding(FragmentSearchBinding::bind)
    override val productLiveData
        get() = viewModel.pagedSearchLiveData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setSearchCategory(args.category)
        mcvScan.setDebounceOnClickListener { navController.navigate(fromSearchToScanner()) }
        viewSearch.setSearchListener { viewModel.doSearch(it.toString()) }
        viewSearch.onBackClick = { requireActivity().onBackPressed() }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.productCountLiveData) {
            binding.mtvSearchResult.text = getString(R.string.countProducts, this).spanSearchCount(this)
            binding.llDrugsNotFoundContainer.visibleOrGone(this == 0)
        }
    }
}