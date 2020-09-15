package com.pharmacy.manager.core.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt

val Activity.rootView: ViewGroup
    get() = findViewById(Window.ID_ANDROID_CONTENT)

fun Activity.hideKeyboard() {
    val view = currentFocus
    if (view != null) {
        inputMethodManager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

val Activity.isKeyboardOpen: Boolean
    get() {
        val visibleBounds = Rect()
        val view = rootView.apply { getWindowVisibleDisplayFrame(visibleBounds) }
        return view.height - visibleBounds.height() > convertDpToPx(50F).roundToInt()
    }