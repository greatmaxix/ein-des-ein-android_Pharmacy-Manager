package com.pulse.manager.core.extensions

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import timber.log.Timber

fun EditText.openKeyboard() {
    requestFocus()
    context.inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun EditText.cursorToEnd() {
    val textLength = text().length
    if (textLength > 0) {
        setSelection(textLength)
    }
}

fun EditText.setTextWithCursorToEndAndOpen(text: String) {
    setTextWithCursorToEnd(text)
    Timber.e("Is keyboard open: $isKeyboardOpen")
    if (isKeyboardNotOpen) {
        openKeyboard()
    }
}

fun EditText.setTextWithCursorToEnd(text: String): String {
    setText(text)
    cursorToEnd()
    return ""
}