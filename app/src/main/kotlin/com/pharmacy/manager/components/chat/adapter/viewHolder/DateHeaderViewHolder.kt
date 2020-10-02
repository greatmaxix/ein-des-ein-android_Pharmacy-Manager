package com.pharmacy.manager.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateHeaderViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    override fun bind(item: MessageItem) {
//        itemView.tvDateChat.text = item.asDateHeader().date.toChatDate().capitalize()
    }

    private fun LocalDateTime.toChatDate() = DateTimeFormatter.ofPattern("EEEE HH:mm").format(this)

    companion object {

        fun newInstance(parent: ViewGroup) = DateHeaderViewHolder(parent.inflate(R.layout.item_chat_date_header))
    }
}