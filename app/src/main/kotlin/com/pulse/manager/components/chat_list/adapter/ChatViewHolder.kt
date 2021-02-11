package com.pulse.manager.components.chat_list.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.ItemChatBinding
import java.time.format.DateTimeFormatter

class ChatViewHolder(view: View) : BaseViewHolder<ChatItem>(view) {

    private val binding by viewBinding(ItemChatBinding::bind)
    private val timeFormatter by lazy { DateTimeFormatter.ofPattern("HH:mm") }
    private val borderWidth by lazy { resources.getDimensionPixelSize(R.dimen._2sdp).toFloat() }

    override fun bind(item: ChatItem) = with(binding) {
        ivAvatar.loadCircularImage(item.customer.avatar?.url, borderWidth, context.compatColor(R.color.green))
        // TODO add counter in future
//            tvCounterChatUser.text = item.messagesCount.toString()
//            tvCounterChatUser.visibleOrGone(item.messagesCount != 0)

        mtvName.text = item.customer.name
        mtvMessage.text = item.lastMessage?.let {
            when {
                it.file != null -> context.getString(R.string.attachment_image)
                it.product != null -> context.getString(R.string.attachment_product)
                else -> item.lastMessage.text
            }
        }
        mtvTime.text = item.lastMessage?.createdAt?.let { timeFormatter.format(it) }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ChatViewHolder(parent.inflate(R.layout.item_chat))
    }
}