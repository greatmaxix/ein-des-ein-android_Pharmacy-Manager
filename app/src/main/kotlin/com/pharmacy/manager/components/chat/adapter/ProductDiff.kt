package com.pharmacy.manager.components.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.manager.components.chat.model.TempProduct

class ProductDiff(private val oldList: List<TempProduct>, private val newList: List<TempProduct>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name // TODO id maybe

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}