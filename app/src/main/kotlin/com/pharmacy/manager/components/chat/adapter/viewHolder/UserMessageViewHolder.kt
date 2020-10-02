package com.pharmacy.manager.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_message_pharmacy.view.*

class UserMessageViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    override fun bind(item: MessageItem) {
        itemView.tvMessageChat.text = item.text
    }

    companion object {

        fun newInstance(parent: ViewGroup) = UserMessageViewHolder(parent.inflate(R.layout.item_chat_message_pharmacy))
    }
}