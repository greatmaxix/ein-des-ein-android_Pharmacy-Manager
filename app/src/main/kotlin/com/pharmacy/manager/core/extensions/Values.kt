package com.pharmacy.manager.core.extensions

import android.text.Editable
import android.util.Patterns
import java.text.DecimalFormat

fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

val String.isPasswordLength: Boolean
    get() = length in 6..31

val Editable?.toTrim: String
    get() = if (this == null) "" else trim().toString()

fun Boolean?.falseIfNull() = this ?: false

fun Float.toLongDecimal(): String = DecimalFormat("0.000").format(this)