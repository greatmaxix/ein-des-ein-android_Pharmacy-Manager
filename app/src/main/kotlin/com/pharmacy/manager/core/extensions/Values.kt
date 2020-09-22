package com.pharmacy.manager.core.extensions

import android.graphics.Typeface
import android.text.Editable
import android.text.Html
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.Patterns
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.log10

fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPasswordLength(): Boolean = this.length >= 6

fun Editable?.toTrimString(): String = this?.trim().toString()

fun Boolean?.falseIfNull() = this ?: false

fun Float.toLongDecimal(): String = DecimalFormat("0.000").format(this)

val String.wrapHtml
    get() = String(Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).run {
        CharArray(length).also {
            TextUtils.getChars(this, 0, length, it, 0)
        }
    })

fun String.spanSearchCount(count: Int) = SpannableString(this).apply {
    setSpan(StyleSpan(Typeface.BOLD), 8, 8 + count.length(), 0)
}

fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}