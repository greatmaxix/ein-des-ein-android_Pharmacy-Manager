package com.pharmacy.manager.core.extensions

import android.text.Editable
import android.util.Patterns
import java.text.DecimalFormat

fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPasswordLength(): Boolean = this.length >= 6

fun Editable?.toTrimString(): String = this?.trim().toString()

fun Boolean?.falseIfNull() = this ?: false

fun Float.toLongDecimal(): String = DecimalFormat("0.000").format(this)