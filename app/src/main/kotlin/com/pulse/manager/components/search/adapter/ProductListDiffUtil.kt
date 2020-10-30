package com.pulse.manager.components.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.manager.components.product.model.ProductLite

object ProductListDiffUtil : DiffUtil.ItemCallback<ProductLite>() {

    override fun areItemsTheSame(oldItem: ProductLite, newItem: ProductLite) = oldItem.globalProductId == newItem.globalProductId

    override fun areContentsTheSame(oldItem: ProductLite, newItem: ProductLite) = oldItem == newItem
}