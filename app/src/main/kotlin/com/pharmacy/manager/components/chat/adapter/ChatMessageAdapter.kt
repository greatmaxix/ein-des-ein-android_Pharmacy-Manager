package com.pharmacy.manager.components.chat.adapter

import android.view.ViewGroup
import com.pharmacy.manager.components.chat.adapter.viewHolder.*
import com.pharmacy.manager.components.chat.model.ChatMessage
import com.pharmacy.manager.core.adapter.BaseRecyclerAdapter
import com.pharmacy.manager.core.adapter.BaseViewHolder

class ChatMessageAdapter : BaseRecyclerAdapter<ChatMessage, BaseViewHolder<ChatMessage>>() {

    fun setList(list: MutableList<ChatMessage>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_MESSAGE_USER -> PharmacyMessageViewHolder.newInstance(parent)
            TYPE_MESSAGE_PHARMACY -> UserMessageViewHolder.newInstance(parent)
            TYPE_DATE_HEADER -> DateHeaderViewHolder.newInstance(parent)
            TYPE_ATTACHMENT -> AttachmentViewHolder.newInstance(parent)
            else -> ProductViewHolder.newInstance(parent)
        }

    override fun getItemViewType(position: Int) = items[position].itemType

    companion object {

        const val TYPE_MESSAGE_USER = 1
        const val TYPE_MESSAGE_PHARMACY = 2
        const val TYPE_DATE_HEADER = 3
        const val TYPE_ATTACHMENT = 4
        const val TYPE_PRODUCT = 5
    }
}