package com.pulse.manager.components.product.adapter

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import com.pulse.manager.components.product.model.Picture
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.load

class ImageViewHolder(view: View) : BaseViewHolder<Picture>(view) {

    override fun bind(item: Picture) = (itemView as ImageView).load(item.url)

    companion object {
        fun newInstance(parent: ViewGroup) = ImageViewHolder(ImageView(parent.context)
            .apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) })
    }
}