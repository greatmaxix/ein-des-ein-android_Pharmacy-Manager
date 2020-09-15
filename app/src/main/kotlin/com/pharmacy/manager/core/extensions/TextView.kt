package com.pharmacy.manager.core.extensions

import android.widget.TextView

fun TextView.clearText() {
    text = ""
}

fun TextView.text(): String = text.toString()