package com.pharmacy.manager.components.home.adapter

import android.view.ViewGroup
import com.pharmacy.manager.components.chatList.adapter.ChatViewHolder
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.core.adapter.BaseRecyclerAdapter
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener

class ChatListAdapter(private val itemClick: (ChatItem) -> Unit) : BaseRecyclerAdapter<ChatItem, ChatViewHolder>() {

    fun setList(list: List<ChatItem>) {
        items = list.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener { itemClick.invoke(itemView.tag as ChatItem) }
    }
}