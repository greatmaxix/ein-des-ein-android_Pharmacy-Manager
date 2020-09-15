package com.pharmacy.manager.core.extensions

import android.content.res.TypedArray

inline fun <R> TypedArray.use(block: TypedArray.() -> R) = block(this).also { recycle() }