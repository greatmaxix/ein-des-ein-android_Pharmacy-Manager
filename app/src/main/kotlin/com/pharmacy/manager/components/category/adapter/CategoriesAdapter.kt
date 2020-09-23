package com.pharmacy.manager.components.category.adapter

import android.view.ViewGroup
import com.pharmacy.manager.components.category.adapter.viewHolder.CategoryTileViewHolder
import com.pharmacy.manager.components.category.model.Category
import com.pharmacy.manager.core.adapter.BaseFilterRecyclerAdapter

class CategoriesAdapter(list: MutableList<Category>, private val click: (Category) -> Unit) : BaseFilterRecyclerAdapter<Category, CategoryTileViewHolder>(list) {

    init {
        notifyDataSet(list)
    }

    override fun diffResult(origin: List<Category>, new: List<Category>) = CategoryDiff(origin, new)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryTileViewHolder.newInstance(parent, click)
}