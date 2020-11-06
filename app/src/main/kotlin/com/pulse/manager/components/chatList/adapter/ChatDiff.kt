package com.pulse.manager.components.chatList.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.manager.components.chatList.model.chat.ChatItem

object ChatDiff : DiffUtil.ItemCallback<ChatItem>() {

    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem) = oldItem.id == newItem.id && oldItem.lastMessage?.messageNumber == newItem.lastMessage?.messageNumber

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem) = true
}