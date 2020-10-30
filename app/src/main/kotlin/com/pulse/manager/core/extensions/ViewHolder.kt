package com.pulse.manager.core.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.*
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder

@ColorInt
fun ViewHolder.compatColor(@ColorRes colorRes: Int) = context.compatColor(colorRes)

fun ViewHolder.dimensionPixelSize(@DimenRes dimenRes: Int) =
    resources.getDimensionPixelSize(dimenRes)

fun ViewHolder.dimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun ViewHolder.drawable(@DrawableRes drawableRes: Int) = context.compatDrawable(drawableRes)

fun ViewHolder.drawable(drawableResName: String) = drawableResId(drawableResName)

@DrawableRes
fun ViewHolder.drawableResId(drawableResName: String) = context.drawableByName(drawableResName)

val ViewHolder.context: Context get() = itemView.context

val ViewHolder.resources: Resources get() = context.resources

fun ViewHolder.stringRes(@StringRes resId: Int) = context.getString(resId)

fun ViewHolder.stringRes(@StringRes resId: Int, vararg args: Any?) = context.getString(resId, *args)

fun <T : View> ViewHolder.findView(@IdRes resId: Int) = itemView.findViewById<T?>(resId)

fun ViewHolder.setParams(
    newWidth: Int? = null,
    newHeight: Int? = null,
    rvParams: (LayoutParams.() -> Unit)? = null
) {
    itemView.updateLayoutParams<LayoutParams> {
        newWidth?.let { width = it }
        newHeight?.let { height = it }
        rvParams?.invoke(this)
    }
}