package com.pharmacy.manager.components.category.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.manager.components.category.model.Category

class CategoryDiff(private val oldList: List<Category>, private val newList: List<Category>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].code == newList[newItemPosition].code

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}