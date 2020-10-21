package com.pharmacy.manager.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import com.pharmacy.manager.core.extensions.load
import com.pharmacy.manager.core.extensions.resources
import kotlinx.android.synthetic.main.item_chat_attachment.view.*

class AttachmentViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val radius = resources.getDimension(R.dimen._8sdp).toInt()
    private val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .priority(Priority.HIGH)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))

    override fun bind(item: MessageItem) {
        with(itemView) {
            ivAttachment.load(item.file?.url) {
                apply(requestOptions)
            }
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = AttachmentViewHolder(parent.inflate(R.layout.item_chat_attachment))
    }
}