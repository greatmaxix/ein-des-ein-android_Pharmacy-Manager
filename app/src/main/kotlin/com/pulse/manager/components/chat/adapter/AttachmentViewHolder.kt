package com.pulse.manager.components.chat.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.core.extensions.load
import com.pulse.manager.core.extensions.resources
import com.pulse.manager.databinding.ItemChatAttachmentBinding

class AttachmentViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatAttachmentBinding::bind)

    private val radius = resources.getDimension(R.dimen._8sdp).toInt()
    private val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .priority(Priority.HIGH)
        .format(DecodeFormat.PREFER_ARGB_8888)
        .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))

    override fun bind(item: MessageItem) {
        with(binding) {
            ivAttachment.load(item.file?.url) {
                apply(requestOptions)
            }
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = AttachmentViewHolder(parent.inflate(R.layout.item_chat_attachment))
    }
}