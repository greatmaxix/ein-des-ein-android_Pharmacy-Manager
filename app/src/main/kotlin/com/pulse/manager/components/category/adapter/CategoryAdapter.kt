package com.pulse.manager.components.category.adapter

import android.view.ViewGroup
import com.pulse.manager.components.category.adapter.viewHolder.CategoryViewHolder
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pulse.manager.core.extensions.setDebounceOnClickListener

class CategoryAdapter(private val click: (Category) -> Unit) : BaseFilterRecyclerAdapter<Category, CategoryViewHolder>() {

    override fun diffResult(origin: List<Category>, new: List<Category>) = CategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder.newInstance(parent)
        .apply {
            itemView.setDebounceOnClickListener { click(getItem(bindingAdapterPosition)) }
        }
}