package com.pharmacy.manager.components.chatList.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.manager.components.chatList.model.ChatItem

object ChatDiff : DiffUtil.ItemCallback<ChatItem>() {

    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem) = true
}