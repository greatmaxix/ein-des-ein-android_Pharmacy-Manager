package com.pulse.manager.components.product.adapter

import android.view.ViewGroup
import com.pulse.manager.components.product.model.Picture
import com.pulse.manager.core.adapter.BaseRecyclerAdapter

class ProductsImageAdapter(items: List<Picture>) : BaseRecyclerAdapter<Picture, ImageViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder.newInstance(parent)
}