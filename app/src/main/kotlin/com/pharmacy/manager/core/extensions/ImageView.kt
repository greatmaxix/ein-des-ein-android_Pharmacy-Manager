package com.pharmacy.manager.core.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

val ImageView.createGlide
    get() = Glide.with(this)

fun ImageView.load(url: String, block: (RequestBuilder<Drawable>.() -> Unit)? = null) {
    val glide = createGlide.load(url)
    block?.let { glide.apply(it).into(this) } ?: glide.into(this)
}

fun ImageView.load(@DrawableRes resId: Int, block: (RequestBuilder<Drawable>.() -> Unit)? = null) {
    val glide = createGlide.load(resId)
    block?.let { glide.apply(it).into(this) } ?: glide.into(this)
}