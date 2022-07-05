package com.pulse.manager.components.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.manager.components.product.model.ProductLite

class ProductDiff(private val oldList: List<ProductLite>, private val newList: List<ProductLite>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].globalProductId == newList[newItemPosition].globalProductId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}