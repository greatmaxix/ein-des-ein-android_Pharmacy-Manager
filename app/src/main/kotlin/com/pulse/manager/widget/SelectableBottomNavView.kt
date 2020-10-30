package com.pulse.manager.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.navigation.NavDestination
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pulse.manager.R
import com.pulse.manager.core.extensions.compatColor
import com.pulse.manager.core.extensions.compatDrawable
import com.pulse.manager.core.extensions.createBitmapWithBorder

class SelectableBottomNavView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private val selectedBorder = resources.getDimensionPixelSize(R.dimen._2sdp).toFloat()
    private val selectedColor = context.compatColor(R.color.primaryBlue)
    private val defaultColor = context.compatColor(R.color.grey)
    private var lastSelectedItem: NavItem? = null
    var navItems: List<NavItem> = listOf()
        set(value) {
            field = value
            updateProfileIconState()
            value.forEach {
                setItemColor(it, defaultColor)
            }
        }

    private fun setItemColor(it: NavItem, @ColorInt color: Int) {
        if (it.iconResId != null) {
            menu.findItem(it.menuItemId)?.icon = context.compatDrawable(it.iconResId)?.apply {
                DrawableCompat.setTint(this, color)
            }
        }
    }

    private fun updateProfileIconState(selected: Boolean = false) {
        val item = navItems.firstOrNull { it.iconUrl != null }
        if (item != null) {
            if (item.iconUrl.isNullOrEmpty().not()) {
                Glide.with(context)
                    .asBitmap()
                    .load(item.iconUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_avatar_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(object : CustomTarget<Bitmap>(AVATAR_SIZE, AVATAR_SIZE) {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            menu.findItem(item.menuItemId)?.icon = resource.run {
                                RoundedBitmapDrawableFactory.create(
                                    resources,
                                    if (selected) createBitmapWithBorder(selectedBorder, selectedColor) else this
                                ).apply {
                                    isCircular = true
                                }
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // no op
                        }
                    })
            } else {
                menu.findItem(item.menuItemId)?.icon = context.compatDrawable(R.drawable.ic_avatar_placeholder)
            }
        }
    }

    fun changeSelection(destination: NavDestination) {
        if (navItems.isEmpty()) throw Throwable("You must set navItems first")

        val currentNavItem = navItems.find { it.navigationItemResId == destination.id }
        lastSelectedItem?.let {
            when {
                it.iconUrl != null -> updateProfileIconState(false)
                else -> setItemColor(it, defaultColor)
            }
        }
        currentNavItem?.let {
            when {
                it.iconUrl != null -> updateProfileIconState(true)
                else -> setItemColor(it, selectedColor)
            }
        }

        lastSelectedItem = currentNavItem
    }

    data class NavItem(
        @IdRes val menuItemId: Int,
        @IdRes val navigationItemResId: Int,
        @DrawableRes val iconResId: Int?,
        val iconUrl: String?
    )

    companion object {

        private const val AVATAR_SIZE = 128
    }
}