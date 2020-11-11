package com.pulse.manager.components.category.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.manager.R
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.gone
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.core.extensions.visible
import kotlinx.android.synthetic.main.item_category.*

class CategoryViewHolder(view: View) : BaseViewHolder<Category>(view) {

    override fun bind(item: Category) = with(itemView) {
        tvCategoryNameTile.text = item.name
        if (item.drawableName != -1) {
            ivCategoryIconTile.setImageResource(item.drawableName)
            ivCategoryIconTile.visible()
        } else {
            ivCategoryIconTile.gone()
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup) = CategoryViewHolder(parent.inflate(R.layout.item_category))
    }
}