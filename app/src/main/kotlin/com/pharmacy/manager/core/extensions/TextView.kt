package com.pharmacy.manager.core.extensions

import android.widget.TextView

fun TextView.clearText() {
    text = ""
}

fun TextView.text(): String = text.toString()

fun TextView.setTextHtml(text: String?) = setText(text?.wrapHtml, TextView.BufferType.SPANNABLE)