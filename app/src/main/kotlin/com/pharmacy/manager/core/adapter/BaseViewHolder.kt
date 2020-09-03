package com.pharmacy.manager.core.adapter

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pharmacy.manager.core.extensions.compatColor
import com.pharmacy.manager.core.extensions.dimensionPixelSize
import com.pharmacy.manager.core.extensions.drawable
import com.pharmacy.manager.core.extensions.stringRes
import kotlinx.android.extensions.LayoutContainer
import timber.log.Timber

abstract class BaseViewHolder<T>(override val containerView: View) : ViewHolder(containerView),
    LayoutContainer {

    abstract fun bind(item: T)

    protected val Int.toText get() = stringRes(this)

    protected val Int.toDrawable
        get() = try {
            drawable(this)
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
            null
        }

    protected val Int.toColor get() = compatColor(this)

    protected val Int.toPixelSize get() = dimensionPixelSize(this)
}