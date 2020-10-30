package com.pulse.manager.components.scanner.adapter

import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.search.adapter.ProductListViewHolder
import com.pulse.manager.core.adapter.BaseRecyclerAdapter
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import kotlinx.coroutines.FlowPreview
import org.koin.core.KoinExperimentalAPI

class ProductListScannerAdapter(private val itemClick: (Int) -> Unit, list: MutableList<ProductLite>) :
    BaseRecyclerAdapter<ProductLite, ProductListViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener { itemClick(getItem(bindingAdapterPosition).globalProductId) }
    }

    @FlowPreview
    @KoinExperimentalAPI
    @ExperimentalPagingApi
    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) = holder.bind(getItem(position))
}