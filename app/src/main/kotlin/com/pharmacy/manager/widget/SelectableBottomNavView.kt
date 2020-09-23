package com.pharmacy.manager.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.navigation.NavDestination
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pharmacy.manager.R
import com.pharmacy.manager.core.extensions.compatColor
import com.pharmacy.manager.core.extensions.createBitmapWithBorder

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
        }

    private fun updateProfileIconState(selected: Boolean = false) {
        val item = navItems.firstOrNull { it.iconUrl != null }
        if (item != null) {
            Glide.with(context)
                .asBitmap()
                .load(item.iconUrl)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(object : CustomTarget<Bitmap>(AVATAR_SIZE, AVATAR_SIZE) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        menu.findItem(item.menuItemId)?.icon = resource.run {
                            RoundedBitmapDrawableFactory.create(resources, if (selected) createBitmapWithBorder(selectedBorder, context.compatColor(R.color.primaryBlue)) else this)
                                .apply {
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

    fun changeSelection(destination: NavDestination) {
        if (navItems.isEmpty()) throw Throwable("You must set navItems first")

        val currentNavItem = navItems.find { it.navigationItemResId == destination.id }
        lastSelectedItem?.let {
            if (it.iconUrl != null) {
                updateProfileIconState(false)
            } else {
                if (it.iconResId != null) {
                    menu.findItem(it.menuItemId)?.icon = ContextCompat.getDrawable(context, it.iconResId)?.apply {
                        DrawableCompat.setTint(this, defaultColor)
                    }
                }
            }
        }
        currentNavItem?.let {
            if (it.iconUrl != null) {
                updateProfileIconState(true)
            } else {
                if (it.iconResId != null) {
                    menu.findItem(destination.id)?.icon = ContextCompat.getDrawable(context, it.iconResId)?.apply {
                        DrawableCompat.setTint(this, selectedColor)
                    }
                }
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