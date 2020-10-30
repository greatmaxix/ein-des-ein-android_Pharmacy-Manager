package com.pulse.manager.components.search.adapter

import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import androidx.paging.ItemSnapshotList
import androidx.paging.PagingDataAdapter
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import kotlinx.coroutines.FlowPreview
import org.koin.core.KoinExperimentalAPI

class ProductListAdapter(private val itemClick: (Int) -> Unit) :
    PagingDataAdapter<ProductLite, ProductListViewHolder>(ProductListDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener { itemClick(getItem(bindingAdapterPosition)!!.globalProductId) }
    }

    @KoinExperimentalAPI
    @ExperimentalPagingApi
    @FlowPreview
    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    private fun <T> ItemSnapshotList<T>.findItemWithPosition(predicate: (T?) -> Boolean) = find(predicate).run { this to indexOf(this) }
}