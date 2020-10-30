package com.pulse.manager.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_date_header.view.*

class DateHeaderViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    override fun bind(item: MessageItem) {
        itemView.tvDateChat.text = item.text
    }

    companion object {

        fun newInstance(parent: ViewGroup) = DateHeaderViewHolder(parent.inflate(R.layout.item_chat_date_header))
    }
}