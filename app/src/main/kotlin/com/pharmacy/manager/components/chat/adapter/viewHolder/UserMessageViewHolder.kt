package com.pharmacy.manager.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.model.ChatMessage
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_message_pharmacy.view.*

class UserMessageViewHolder(itemView: View) : BaseViewHolder<ChatMessage>(itemView) {

    override fun bind(item: ChatMessage) {
        itemView.tvMessageChat.text = item.asPharmacyMessage().message
    }

    companion object {

        fun newInstance(parent: ViewGroup) = UserMessageViewHolder(parent.inflate(R.layout.item_chat_message_pharmacy))
    }
}