package com.pharmacy.manager.components.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.manager.components.product.model.Product

class ProductDiff(private val oldList: List<Product>, private val newList: List<Product>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].globalProductId == newList[newItemPosition].globalProductId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}