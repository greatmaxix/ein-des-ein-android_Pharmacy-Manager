package com.pulse.manager.components.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pulse.manager.R
import com.pulse.manager.components.search.SearchFragmentDirections.Companion.fromSearchToScanner
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.core.extensions.spanSearchCount
import com.pulse.manager.core.extensions.visibleOrGone
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SearchFragment(private val viewModel: SearchViewModel) : BaseProductListFragment<SearchViewModel>(R.layout.fragment_search, viewModel) {

    private val args by navArgs<SearchFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setSearchCategory(args.category)
        mcvScanSearch.setDebounceOnClickListener { navController.navigate(fromSearchToScanner()) }

        searchView.setSearchListener { viewModel.doSearch(it.toString()) }

        searchView.onBackClick = {
            requireActivity().onBackPressed()
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.productCountLiveData) {
            tvSearchResult.text = getString(R.string.countProducts, this).spanSearchCount(this)
            llDrugsNotFoundContainer.visibleOrGone(this == 0)
        }
    }

    override val productLiveData
        get() = viewModel.pagedSearchLiveData
}