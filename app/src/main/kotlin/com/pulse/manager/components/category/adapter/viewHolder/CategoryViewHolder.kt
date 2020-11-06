package com.pulse.manager.components.category.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.manager.R
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryViewHolder(view: View, val click: (Category) -> Unit) : BaseViewHolder<Category>(view) {

    override fun bind(item: Category) = with(itemView) {
        tvCategoryName.text = item.name
        mcvCategory.setDebounceOnClickListener { click(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, click: (Category) -> Unit) = CategoryViewHolder(parent.inflate(R.layout.item_category), click)
    }
}