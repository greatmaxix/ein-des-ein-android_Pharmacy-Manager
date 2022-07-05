package com.pulse.manager.components.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.manager.components.product.model.ProductLite

object ProductAttachSearchDiff : DiffUtil.ItemCallback<ProductLite>() {

    override fun areItemsTheSame(oldItem: ProductLite, newItem: ProductLite) = oldItem.globalProductId == newItem.globalProductId

    override fun areContentsTheSame(oldItem: ProductLite, newItem: ProductLite) = oldItem == newItem
}