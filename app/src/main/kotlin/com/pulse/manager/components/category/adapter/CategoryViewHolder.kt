package com.pulse.manager.components.category.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.gone
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.core.extensions.visible
import com.pulse.manager.databinding.ItemCategoryBinding

class CategoryViewHolder(view: View) : BaseViewHolder<Category>(view) {

    private val binding by viewBinding(ItemCategoryBinding::bind)

    override fun bind(item: Category) = with(binding) {
        mtvCategoryName.text = item.name
        if (item.drawableName != -1) {
            ivCategoryIcon.setImageResource(item.drawableName)
            ivCategoryIcon.visible()
        } else {
            ivCategoryIcon.gone()
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = CategoryViewHolder(parent.inflate(R.layout.item_category))
    }
}