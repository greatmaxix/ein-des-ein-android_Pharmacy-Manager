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
            value.resetSelection()
            val firstItem = value.first()
            changeSelection(firstItem.navigationItemResId)
        }

    private fun setItemColor(it: NavItem, @ColorInt color: Int) {
        if (it.iconResId != null) {
            menu.findItem(it.menuItemId)?.icon = context.compatDrawable(it.iconResId)?.apply {
                DrawableCompat.setTint(this, color)
            }
        }
    }

    private fun updateProfileIconState(selected: Boolean = false) {
        val item = navItems.firstOrNull { it.isProfileItem }
        if (item != null) {
            Glide.with(context)
                .asBitmap()
                .load(item.iconUrl ?: R.drawable.ic_avatar_placeholder)
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
        }
    }

    fun changeSelection(@IdRes destinationId: Int) {
        if (navItems.isEmpty()) throw Throwable("You must set navItems first")

        val currentNavItem = navItems.find { it.navigationItemResId == destinationId } ?: return
        lastSelectedItem?.let {
            when {
                it.isProfileItem -> updateProfileIconState(false)
                else -> setItemColor(it, defaultColor)
            }
        }
        when {
            currentNavItem.isProfileItem -> updateProfileIconState(true)
            else -> setItemColor(currentNavItem, selectedColor)
        }
        lastSelectedItem = currentNavItem
    }

    private fun List<NavItem>.resetSelection() {
        forEach {
            if (it.isProfileItem) updateProfileIconState(false)
            else setItemColor(it, defaultColor)
        }
    }

    data class NavItem(
        @IdRes val menuItemId: Int,
        @IdRes val navigationItemResId: Int,
        @DrawableRes val iconResId: Int?,
        val isProfileItem: Boolean = false,
        val iconUrl: String?
    )

    companion object {

        private const val AVATAR_SIZE = 128
    }
}