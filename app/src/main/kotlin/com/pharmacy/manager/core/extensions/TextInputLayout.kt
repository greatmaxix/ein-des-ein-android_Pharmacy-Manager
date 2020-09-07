package com.pharmacy.manager.core.extensions

import com.google.android.material.textfield.TextInputLayout
import com.pharmacy.manager.R

fun TextInputLayout.changeFieldState(lastState: Boolean, isValid: Boolean, error: String?): Boolean {
    return when {
        isValid && !lastState -> {
            editText?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_field_accept, 0)
            setBoxStrokeColorStateList(context.getColorStateList(R.color.selector_til_stroke_accepter))
            post { endIconMode = TextInputLayout.END_ICON_NONE }
            true
        }
        !error.isNullOrBlank() -> {
            editText?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_field_error, 0)
            setBoxStrokeColorStateList(context.getColorStateList(R.color.selector_til_stroke_failed))
            post { endIconMode = TextInputLayout.END_ICON_NONE }
            false
        }
        !isValid && lastState -> {
            editText?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            setBoxStrokeColorStateList(context.getColorStateList(R.color.mtrl_outlined_stroke_color))
            post { endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT }
            false
        }
        else -> false
    }
}