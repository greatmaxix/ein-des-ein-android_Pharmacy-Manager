package com.pharmacy.manager.core.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isEmpty
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.pharmacy.manager.R

fun View.animateVisible(duration: Long = 100) {
    alpha = 0f
    isGone = false
    animate().setDuration(duration).alpha(1f).withEndAction { alpha = 1f }
}

fun View.animateGone(duration: Long = 100) {
    animate().setDuration(duration).alpha(0f).withEndAction { isGone = true }
}


fun View.animateInvisible(duration: Long = 100) {
    animate().setDuration(duration).alpha(0f).withEndAction { isInvisible = true }
}

fun View.animateGoneIfNot(duration: Long = 100) {
    if (visibility != GONE) {
        animateGone(duration)
    }
}

fun View.animateVisibleIfNot(duration: Long = 100) {
    if (visibility != VISIBLE) {
        animateVisible(duration)
    }
}

inline fun View.onClick(crossinline f: () -> Unit) {
    setOnClickListener { f.invoke() }
}

fun View.hideKeyboard(needClearFocus: Boolean = true) =
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        .also { if (needClearFocus) clearFocus() }

fun View.setDebounceOnClickListener(interval: Long = 400, listener: View.() -> Unit) {
    var lastClick = 0L
    setOnClickListener { v ->
        SystemClock.uptimeMillis().apply {
            if (minus(lastClick) > interval) listener.invoke(v)
            lastClick = this
        }
    }
}

val View.isKeyboardOpen
    get() = Rect().run {
        val screenHeight = rootView.height
        getWindowVisibleDisplayFrame(this)
        screenHeight - bottom > screenHeight * 0.15
    }

@RequiresApi(Build.VERSION_CODES.M)
fun View.colorFrom(@ColorRes colorRes: Int) = context.getColor(colorRes)

fun Toolbar.setMenu(@MenuRes menu: Int, itemClick: Toolbar.OnMenuItemClickListener? = null) {
    if (this.menu.isEmpty()) {
        inflateMenu(menu)
        setOnMenuItemClickListener(itemClick)
    }
}

fun View.toggle(
    needAnim: Boolean = false,
    duration: Long = 250,
    action: ((animator: Animator) -> Unit)? = null
) {
    if (needAnim) {
        translationYAnim(if (translationY > 0) 0f else height.toFloat(), duration, action)
    } else {
        translationY = if (translationY > 0) 0f else height.toFloat()
    }
}

fun View.translationYAnim(
    value: Float,
    duration: Long = 250,
    action: ((animator: Animator) -> Unit)? = null
) {
    ObjectAnimator.ofFloat(this, "translationY", value)
        .apply {
            this.duration = duration
            action?.let { doOnEnd(it) }
            start()
        }
}

fun View.translateYDown(duration: Long = resources.getInteger(R.integer.animation_time).toLong()) {
    if (translationY == 0f) {
        toggle(true, duration)
    }
}

fun View.translateYUp(duration: Long = resources.getInteger(R.integer.animation_time).toLong()) {
    if (translationY > 0f) {
        toggle(true, duration)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun TextView.textColor(@ColorRes colorResId: Int): TextView {
    setTextColor(colorFrom(colorResId))
    return this
}

fun View.onGlobalLayout(block: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            block()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun View.setTopRoundCornerBackground(
    radius: Float = resources.getDimension(R.dimen._10sdp),
    elevation: Float = resources.getDimension(R.dimen._4sdp),
    @ColorInt tintColor: Int = ContextCompat.getColor(context, R.color.colorGlobalWhite)
) {
    val appearanceModel = ShapeAppearanceModel()
        .toBuilder()
        .setTopRightCorner(CornerFamily.ROUNDED, radius)
        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
        .build()

    val shape = MaterialShapeDrawable(appearanceModel)
        .apply {
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
            this.elevation = elevation
            setTint(tintColor)
            paintStyle = Paint.Style.FILL
        }

    ViewCompat.setBackground(this, shape)
}