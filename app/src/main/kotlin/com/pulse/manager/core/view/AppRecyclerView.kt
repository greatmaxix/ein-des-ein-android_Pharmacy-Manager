package com.pulse.manager.core.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class AppRecyclerView<VH : RecyclerView.ViewHolder, A : RecyclerView.Adapter<VH>> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    @Suppress("UNCHECKED_CAST")
    override fun getAdapter() = try {
        super.getAdapter() as A
    } catch (e: Exception) {
        throw Exception("Check your adapter type")
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        throw Exception("For set adapter use function @setAppAdapter@")
    }

    fun setAppAdapter(adapter: A?) = try {
        super.setAdapter(adapter)
    } catch (e: Exception) {
        throw Exception("Check your adapter type")
    }
}