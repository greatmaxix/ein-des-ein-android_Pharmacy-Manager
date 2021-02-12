package com.pulse.manager.components.home.adapter

import android.view.ViewGroup
import com.pulse.manager.components.chat_list.adapter.ChatViewHolder
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.core.adapter.BaseRecyclerAdapter
import com.pulse.manager.core.extensions.setDebounceOnClickListener

class ChatListAdapter(private val itemClick: (ChatItem) -> Unit) : BaseRecyclerAdapter<ChatItem, ChatViewHolder>() {

    fun setList(list: List<ChatItem>) {
        items = list.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener { itemClick(getItem(bindingAdapterPosition)) }
    }
}