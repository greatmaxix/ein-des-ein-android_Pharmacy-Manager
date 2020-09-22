package com.pharmacy.manager.components.product.adapter

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import com.pharmacy.manager.components.product.model.Picture
import com.pharmacy.manager.core.adapter.BaseRecyclerAdapter
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.load

class ProductsImageAdapter(items: List<Picture>) : BaseRecyclerAdapter<Picture, ProductsImageAdapter.ImageViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder = ImageViewHolder.newInstance(parent)

    class ImageViewHolder(view: View) : BaseViewHolder<Picture>(view) {

        override fun bind(item: Picture) = (itemView as ImageView).load(item.url)

        companion object {
            fun newInstance(parent: ViewGroup) = ImageViewHolder(ImageView(parent.context)
                .apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) })
        }
    }
}