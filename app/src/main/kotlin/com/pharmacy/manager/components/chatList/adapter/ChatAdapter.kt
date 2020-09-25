package com.pharmacy.manager.components.chatList.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chatList.model.TempChat
import com.pharmacy.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.*
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(private val itemClick: (TempChat) -> Unit) : BaseFilterRecyclerAdapter<TempChat, ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatViewHolder.newInstance(parent, itemClick)

    override fun diffResult(origin: List<TempChat>, new: List<TempChat>) = ChatDiff(origin, new)

    class ChatViewHolder(view: View, listener: (TempChat) -> Unit) : BaseViewHolder<TempChat>(view) {

        private var borderWidth = resources.getDimensionPixelSize(R.dimen._2sdp).toFloat()

        init {
            itemView.setDebounceOnClickListener { listener.invoke(itemView.tag as TempChat) }
        }

        override fun bind(item: TempChat) = with(itemView) {
            tag = item
            ivAvatarChatUser.loadCircularImage(item.avatar, borderWidth, context.compatColor(R.color.green))
            tvCounterChatUser.text = item.messagesCount.toString()
            tvCounterChatUser.visibleOrGone(item.messagesCount != 0)
            tvNameChatUser.text = item.name
            tvMessageChatUser.text = item.lastMessage
            tvTimeChatUser.text = item.time
        }

        companion object {

            fun newInstance(parent: ViewGroup, listener: (TempChat) -> Unit) = ChatViewHolder(parent.inflate(R.layout.item_chat), listener)
        }
    }
}