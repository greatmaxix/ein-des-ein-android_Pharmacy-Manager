package com.pulse.manager.components.chatList.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.manager.components.chatList.model.chat.ChatItem
import com.pulse.manager.core.extensions.setDebounceOnClickListener

class ChatListPagingAdapter(private val itemClick: (ChatItem) -> Unit) : PagingDataAdapter<ChatItem, ChatViewHolder>(ChatDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener { itemClick(itemView.tag as ChatItem) }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }
}