package com.pharmacy.manager.components.chatList.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.manager.components.chatList.model.TempChat

class ChatDiff(private val oldList: List<TempChat>, private val newList: List<TempChat>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].name == newList[newItemPosition].name // TODO id maybe

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}