package com.pulse.manager.components.chat.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.font
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.databinding.ItemChatDateHeaderBinding

class DateHeaderViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatDateHeaderBinding::bind)
    private val typefaceSemibold = itemView.font(R.font.open_sans_semi_bold)

    override fun bind(item: MessageItem) {
        binding.mtvDate.text = SpannableString(item.text).apply {
            val split = item.text?.split(",")
            setSpan(StyleSpan(typefaceSemibold.style), 0, split?.first()?.length ?: 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = DateHeaderViewHolder(parent.inflate(R.layout.item_chat_date_header))
    }
}