package com.pulse.manager.components.needHelp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.needHelp.model.HelpItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.databinding.ItemHelpBinding

class HelpViewHolder(view: View) : BaseViewHolder<HelpItem>(view) {

    private val binding by viewBinding(ItemHelpBinding::bind)

    override fun bind(item: HelpItem) = with(binding) {
        itemHeader.icon = item.icon
        itemHeader.title = item.title
        mtvText.text = item.text
        mtvText.isVisible = item.isExpanded
        itemHeader.isSelected = item.isExpanded
    }

    fun changeExpandState(item: HelpItem) = with(binding) {
        mtvText.isVisible = item.isExpanded
        itemHeader.isSelected = item.isExpanded
    }

    companion object {

        fun newInstance(parent: ViewGroup) = HelpViewHolder(parent.inflate(R.layout.item_help))
    }
}