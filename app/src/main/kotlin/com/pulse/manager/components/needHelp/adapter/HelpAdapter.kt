package com.pulse.manager.components.needHelp.adapter

import android.view.ViewGroup
import com.pulse.manager.components.needHelp.model.HelpItem
import com.pulse.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pulse.manager.core.extensions.setDebounceOnClickListener

class HelpAdapter : BaseFilterRecyclerAdapter<HelpItem, HelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HelpViewHolder.newInstance(parent).apply {
        itemView.setDebounceOnClickListener {
            val item = getItem(bindingAdapterPosition)
            item.isExpanded = !item.isExpanded
            changeExpandState(item)
        }
    }

    override fun diffResult(origin: List<HelpItem>, new: List<HelpItem>) = HelpDiff(origin, new)
}