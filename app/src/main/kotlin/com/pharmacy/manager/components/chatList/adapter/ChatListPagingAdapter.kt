package com.pharmacy.manager.components.chatList.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener

class ChatListPagingAdapter(private val itemClick: (ChatItem) -> Unit) : PagingDataAdapter<ChatItem, ChatViewHolder>(ChatDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener { itemClick.invoke(itemView.tag as ChatItem) }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}