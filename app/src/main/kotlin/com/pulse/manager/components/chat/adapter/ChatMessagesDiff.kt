package com.pulse.manager.components.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.manager.components.chat.model.message.MessageItem

object ChatMessagesDiff : DiffUtil.ItemCallback<MessageItem>() {

    override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem) = (oldItem.id == newItem.id && oldItem.createdAt.isEqual(newItem.createdAt))

    override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem) = true
}