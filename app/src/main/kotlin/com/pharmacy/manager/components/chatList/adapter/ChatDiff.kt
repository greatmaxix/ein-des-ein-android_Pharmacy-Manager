package com.pharmacy.manager.components.chatList.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.manager.components.chatList.model.chat.ChatItem

object ChatDiff : DiffUtil.ItemCallback<ChatItem>() {

    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem) = oldItem.id == newItem.id && oldItem.lastMessage == newItem.lastMessage

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem) = true
}