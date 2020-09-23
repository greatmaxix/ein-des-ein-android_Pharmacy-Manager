package com.pharmacy.manager.components.category.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.category.model.Category
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_category_tile.view.*

class CategoryTileViewHolder(view: View, val click: (Category) -> Unit) : BaseViewHolder<Category>(view) {

    override fun bind(item: Category) {
        itemView.tvCategoryNameTile.text = item.name
        itemView.mcvCategoryTile.setDebounceOnClickListener { click(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, click: (Category) -> Unit) = CategoryTileViewHolder(parent.inflate(R.layout.item_category_tile), click)
    }
}