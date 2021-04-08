package com.pulse.manager.core.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.pulse.manager.R
import com.pulse.manager.core.base.fragment.dialog.AlertDialogData
import com.pulse.manager.core.base.fragment.dialog.AlertDialogDataRes
import com.pulse.manager.core.base.fragment.dialog.AlertDialogFragment
import kotlin.math.roundToInt

fun FragmentActivity.lazyNavController(@IdRes resId: Int = R.id.nav_host) = lazyNotSynchronized {
    try {
        (supportFragmentManager.findFragmentById(resId) as NavHostFragment).navController
    } catch (e: IllegalArgumentException) {
        throw IllegalArgumentException("${this::class.java.simpleName} does not use \"navController\"")
    }
}

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

fun FragmentActivity.showAlert(message: String, block: AlertDialogData.() -> Unit) =
    AlertDialogData().apply(block).run { alert(message, block).show(supportFragmentManager) }

fun FragmentActivity.showAlertRes(message: String, block: AlertDialogDataRes.() -> Unit) =
    AlertDialogDataRes().apply(block).run { alertRes(message, block).show(supportFragmentManager) }

fun Activity.alert(message: String, block: AlertDialogData.() -> Unit) = AlertDialogData().apply(block).run {
    AlertDialogFragment.newInstance(title, message, positive, negative)
        .apply {
            setNegativeListener { dialog, _ ->
                negativeAction?.invoke()
                dialog.dismiss()
            }
            setPositiveListener { _, _ -> positiveAction?.invoke() }
            isCancelable = cancelable
        }
}

fun Activity.alertRes(message: String, block: AlertDialogDataRes.() -> Unit) = AlertDialogDataRes().apply(block).run {
    AlertDialogFragment.newInstance(title?.let { getString(it) }, message, positive?.let { getString(it) }, negative?.let { getString(it) })
        .apply {
            setNegativeListener { dialog, _ ->
                negativeAction?.invoke()
                dialog.dismiss()
            }
            setPositiveListener { _, _ -> positiveAction?.invoke() }
            isCancelable = cancelable
        }
}

fun Activity.font(@FontRes fontId: Int) = resources.getFont(fontId)