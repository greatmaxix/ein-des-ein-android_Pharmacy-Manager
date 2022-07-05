package com.pulse.manager.components.chat.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate

class EndChatViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    override fun bind(item: MessageItem) {
        // no op
    }

    companion object {

        fun newInstance(parent: ViewGroup) = EndChatViewHolder(parent.inflate(R.layout.item_chat_end))
    }
}