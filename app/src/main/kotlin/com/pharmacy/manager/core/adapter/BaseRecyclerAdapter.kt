package com.pharmacy.manager.core.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T, VH : BaseViewHolder<T>>(
    list: List<T> = mutableListOf(),
    private val needNotifyList: Boolean = true
) : RecyclerView.Adapter<VH>() {

    protected var items: MutableList<T> = list.toMutableList()
        set(value) {
            field = value
            if (needNotifyList) {
                notifyDataSetChanged()
            }
        }

    protected var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    fun getItem(position: Int) = items[position]

    fun setItem(position: Int, item: T) {
        items[position] = item
    }

    fun setItem(item: T) {
        items[getItemPosition(item)] = item
    }

    fun setItemNotify(item: T) {
        val position = getItemPosition(item)
        items[position] = item
        notifyItemChanged(position)
    }

    fun getItemPosition(item: T) = items.indexOf(item)

    val isEmpty
        get() = itemCount == 0
}