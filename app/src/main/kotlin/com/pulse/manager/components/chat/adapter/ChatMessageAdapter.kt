package com.pulse.manager.components.chat.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder

class ChatMessageAdapter : PagingDataAdapter<MessageItem, BaseViewHolder<MessageItem>>(ChatMessagesDiff) {

    override fun onBindViewHolder(holder: BaseViewHolder<MessageItem>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_MESSAGE_USER -> PharmacyMessageViewHolder.newInstance(parent)
            TYPE_MESSAGE_PHARMACY -> UserMessageViewHolder.newInstance(parent)
            TYPE_DATE_HEADER -> DateHeaderViewHolder.newInstance(parent)
            TYPE_ATTACHMENT -> AttachmentViewHolder.newInstance(parent)
            TYPE_PRODUCT -> ProductViewHolder.newInstance(parent)
            else -> EndChatViewHolder.newInstance(parent)
        }

    override fun getItemViewType(position: Int) = getItem(position)?.messageType ?: -1

    companion object {

        const val TYPE_MESSAGE_USER = 1
        const val TYPE_MESSAGE_PHARMACY = 2
        const val TYPE_DATE_HEADER = 3
        const val TYPE_ATTACHMENT = 4
        const val TYPE_PRODUCT = 5
        const val TYPE_END_CHAT = 6
    }
}