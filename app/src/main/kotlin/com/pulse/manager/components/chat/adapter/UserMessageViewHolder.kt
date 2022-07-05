package com.pulse.manager.components.chat.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.databinding.ItemChatMessagePharmacyBinding

class UserMessageViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatMessagePharmacyBinding::bind)

    override fun bind(item: MessageItem) {
        binding.mtvMessage.text = item.text
    }

    companion object {

        fun newInstance(parent: ViewGroup) = UserMessageViewHolder(parent.inflate(R.layout.item_chat_message_pharmacy))
    }
}