package com.pharmacy.manager.components.scanner.adapter

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pharmacy.manager.R
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.*
import kotlinx.android.synthetic.main.item_product_search.view.*
import kotlinx.android.synthetic.main.item_product_short.view.tvProductPrice

class ProductListViewHolder(override val containerView: View) : BaseViewHolder<ProductLite>(containerView) {

    override fun bind(item: ProductLite) = with(item) {

        if (pictures.isNotEmpty()) {
            itemView.ivProduct.load(pictures.first().url) {
                transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize))
            }
        }

        itemView.tvTitle.setTextHtml(rusName)
        itemView.tvSubTitle.setTextHtml(releaseForm)

        itemView.tvManufacture.setTextHtml(stringRes(R.string.manufacture, productLocale))

        aggregation?.let { itemView.tvProductPrice.text = stringRes(R.string.price, it.minPrice) }
        itemView.tvPricePrefix.visibleOrGone(aggregation != null)
        itemView.tvPriceUnavailable.visibleOrGone(aggregation == null)

    }

    companion object {
        fun newInstance(parent: ViewGroup) = ProductListViewHolder(parent.inflate(R.layout.item_product_search))
    }
}