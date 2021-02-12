package com.pulse.manager.components.chat.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.setDebounceOnClickListener

class ProductAttachSearchAdapter(private val itemClick: (ProductLite) -> Unit) : PagingDataAdapter<ProductLite, BaseViewHolder<ProductLite>>(ProductAttachSearchDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductAttachViewHolder.newInstance(parent)
        .apply { itemView.setDebounceOnClickListener { getItem(bindingAdapterPosition)?.let(itemClick) } }

    override fun onBindViewHolder(holder: BaseViewHolder<ProductLite>, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}