package com.pulse.manager.components.chat.adapter.viewHolder

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.font
import com.pulse.manager.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_date_header.view.*

class DateHeaderViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val typefaceSemibold = itemView.font(R.font.open_sans_semi_bold)

    override fun bind(item: MessageItem) {
        val split = item.text?.split(",")
        val span = SpannableString(item.text)
        span.setSpan(StyleSpan(typefaceSemibold.style), 0, split?.first()?.length ?: 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        itemView.tvDateChat.text = span
    }

    companion object {

        fun newInstance(parent: ViewGroup) = DateHeaderViewHolder(parent.inflate(R.layout.item_chat_date_header))
    }
}