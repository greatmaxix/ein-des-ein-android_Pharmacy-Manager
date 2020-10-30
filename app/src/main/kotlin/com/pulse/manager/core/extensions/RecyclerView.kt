package com.pulse.manager.core.extensions

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.pulse.manager.R
import com.pulse.manager.core.adapter.GridSpacingItemDecoration

fun RecyclerView.addDrawableItemDivider(
    @DrawableRes drawableRes: Int,
    orientation: Int = DividerItemDecoration.VERTICAL
) {
    context.compatDrawable(drawableRes)?.let {
        addItemDecoration(DividerItemDecoration(context, orientation).apply { setDrawable(it) })
    }
}

fun RecyclerView.onScrollStateChanged(listener: (RecyclerView, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
            listener.invoke(recyclerView, state)
        }
    })
}

fun RecyclerView.onScrollStateChanged(listener: (Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
            listener.invoke(state)
        }
    })
}

fun RecyclerView.onScrolled(listener: (RecyclerView, Int, Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            listener.invoke(recyclerView, dx, dy)
        }
    })
}

fun RecyclerView.addAutoKeyboardCloser() {
    onScrollStateChanged { state ->
        if (state == RecyclerView.SCROLL_STATE_DRAGGING && isKeyboardOpen) {
            hideKeyboard()
        }
    }
}

fun RecyclerView.addGridItemDecorator() = addItemDecoration(GridSpacingItemDecoration(spacing = context.resources.getDimensionPixelSize(R.dimen._8sdp)))