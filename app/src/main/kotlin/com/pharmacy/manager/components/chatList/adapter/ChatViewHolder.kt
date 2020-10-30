package com.pharmacy.manager.components.chatList.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.compatColor
import com.pharmacy.manager.core.extensions.inflate
import com.pharmacy.manager.core.extensions.loadCircularImage
import com.pharmacy.manager.core.extensions.resources
import kotlinx.android.synthetic.main.item_chat.view.*
import java.time.format.DateTimeFormatter

class ChatViewHolder(view: View) : BaseViewHolder<ChatItem>(view) {

    private val timeFormatter by lazy { DateTimeFormatter.ofPattern("HH:mm") }
    private var borderWidth = resources.getDimensionPixelSize(R.dimen._2sdp).toFloat()

    override fun bind(item: ChatItem) = with(itemView) {
        tag = item

        ivAvatarChatUser.loadCircularImage(item.customer.avatar?.url, borderWidth, context.compatColor(R.color.green))
        // TODO add counter in future
//            tvCounterChatUser.text = item.messagesCount.toString()
//            tvCounterChatUser.visibleOrGone(item.messagesCount != 0)

        tvNameChatUser.text = item.customer.name
        tvMessageChatUser.text = item.lastMessage?.let {
            when {
                it.file != null -> context.getString(R.string.attachment_image)
                it.product != null -> context.getString(R.string.attachment_product)
                else -> item.lastMessage.text
            }
        }
        tvTimeChatUser.text = item.lastMessage?.createdAt?.let { timeFormatter.format(it) }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ChatViewHolder(parent.inflate(R.layout.item_chat))
    }
}