package com.pulse.manager.core.extensions

import android.graphics.*
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageProduct
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.util.ColorFilterUtil

val ImageView.createGlide
    get() = Glide.with(this)

fun ImageView.load(url: String?, block: (RequestBuilder<Drawable>.() -> Unit)? = null) {
    val glide = createGlide.load(url)
    block?.let { glide.apply(it).into(this) } ?: glide.into(this)
}

fun ImageView.load(@DrawableRes resId: Int, block: (RequestBuilder<Drawable>.() -> Unit)? = null) {
    val glide = createGlide.load(resId)
    block?.let { glide.apply(it).into(this) } ?: glide.into(this)
}

fun <T> ImageView.loadCircularImage(
    model: T,
    borderSize: Float = 0F,
    borderColor: Int = Color.WHITE
) {
    Glide.with(context)
        .asBitmap()
        .load(model)
        .placeholder(R.drawable.ic_avatar_placeholder)
        .apply(RequestOptions.circleCropTransform())
        .into(object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap?) {
                setImageDrawable(
                    resource?.run {
                        RoundedBitmapDrawableFactory.create(
                            resources,
                            if (borderSize > 0) {
                                createBitmapWithBorder(borderSize, borderColor)
                            } else {
                                this
                            }
                        ).apply {
                            isCircular = true
                        }
                    }
                )
            }
        })
}

fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val halfWidth = width / 2
    val halfHeight = height / 2
    val circleRadius = halfWidth.coerceAtMost(halfHeight).toFloat()
    val newBitmap = Bitmap.createBitmap(
        width + borderOffset,
        height + borderOffset,
        Bitmap.Config.ARGB_8888
    )

    val centerX = halfWidth + borderSize
    val centerY = halfHeight + borderSize

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        drawARGB(0, 0, 0, 0)
    }

    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, circleRadius, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)

    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = borderColor
    paint.strokeWidth = borderSize
    canvas.drawCircle(centerX, centerY, circleRadius, paint)
    return newBitmap
}

fun ImageView.setProductImage(product: ProductLite) {
    load(product.pictures.firstOrNull()?.url) {
        transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelSize(R.dimen._8sdp)))
        error(R.drawable.default_product_image)
    }
    val hasPictures = product.pictures.isNotEmpty()
    setBackgroundColor(if (hasPictures) 0 else ContextCompat.getColor(context, R.color.mediumGrey50))
    colorFilter = (if (product.aggregation == null && !hasPictures) ColorFilterUtil.blackWhiteFilter else null)
}

// TODO refactor this
fun ImageView.setProductImage(product: MessageProduct) {
    load(product.pictures.firstOrNull()?.url) {
        transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelSize(R.dimen._8sdp)))
        error(R.drawable.default_product_image)
    }
    val hasPictures = product.pictures.isNotEmpty()
    setBackgroundColor(if (hasPictures) 0 else ContextCompat.getColor(context, R.color.mediumGrey50))
    colorFilter = (if (product.pharmacyProductsAggregationData == null && !hasPictures) ColorFilterUtil.blackWhiteFilter else null)
}