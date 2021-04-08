package com.pulse.manager.components.home.adapter

import android.view.ViewGroup
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.setDebounceOnClickListener

class ProductAdapter(private val itemClick: (ProductLite) -> Unit) : BaseFilterRecyclerAdapter<ProductLite, BaseViewHolder<ProductLite>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder.newInstance(parent)
        .apply { itemView.setDebounceOnClickListener { itemClick(getItem(bindingAdapterPosition)) } }

    override fun diffResult(origin: List<ProductLite>, new: List<ProductLite>) = ProductDiff(origin, new)
}