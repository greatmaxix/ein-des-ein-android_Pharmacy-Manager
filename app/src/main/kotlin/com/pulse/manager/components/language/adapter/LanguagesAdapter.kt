package com.pulse.manager.components.language.adapter

import android.view.ViewGroup
import com.pulse.manager.components.language.model.LanguageAdapterModel
import com.pulse.manager.core.adapter.BaseRecyclerAdapter
import com.pulse.manager.core.locale.LocaleEnum

class LanguagesAdapter(private val itemClick: (LocaleEnum) -> Unit) : BaseRecyclerAdapter<LanguageAdapterModel, LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LanguageViewHolder.newInstance(parent, itemClick)

    fun notifyItems(list: List<LanguageAdapterModel>) {
        items = list.toMutableList()
    }
}