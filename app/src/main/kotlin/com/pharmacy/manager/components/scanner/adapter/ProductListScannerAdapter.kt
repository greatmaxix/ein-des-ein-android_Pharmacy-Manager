package com.pharmacy.manager.components.scanner.adapter

import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.core.adapter.BaseRecyclerAdapter
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener

class ProductListScannerAdapter(private val itemClick: (Int) -> Unit, list: MutableList<ProductLite>) :
    BaseRecyclerAdapter<ProductLite, ProductListViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener { itemClick(getItem(bindingAdapterPosition).globalProductId) }
    }

    @ExperimentalPagingApi
    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) = holder.bind(getItem(position))
}