package com.pulse.manager.components.chat.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.databinding.ItemChatMessageUserBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PharmacyMessageViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatMessageUserBinding::bind)

    override fun bind(item: MessageItem) = with(binding) {
        mtvMessage.text = item.text
//        tvReadTimeChat.text = message.readDate?.toReadDate() // TODO read date
//        tvReadTimeChat.visibleOrGone(message.readDate ! = null)
    }

    private fun LocalDateTime.toReadDate() = itemView.context.getString(R.string.readDateHolder, DateTimeFormatter.ofPattern("HH:mm").format(this))

    companion object {

        fun newInstance(parent: ViewGroup) = PharmacyMessageViewHolder(parent.inflate(R.layout.item_chat_message_user))
    }
}