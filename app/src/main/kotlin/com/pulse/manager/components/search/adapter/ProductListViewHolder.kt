package com.pulse.manager.components.search.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import com.pulse.manager.R
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.*
import kotlinx.android.synthetic.main.item_product_search.view.*
import kotlinx.coroutines.FlowPreview
import org.koin.core.KoinExperimentalAPI

class ProductListViewHolder(override val containerView: View) : BaseViewHolder<ProductLite>(containerView) {

    @KoinExperimentalAPI
    @FlowPreview
    @ExperimentalPagingApi
    override fun bind(item: ProductLite) = with(item) {
        itemView.ivProduct.setProductImage(this)

        itemView.tvTitle.setTextHtml(rusName)
        itemView.tvSubTitle.setTextHtml(releaseForm)

        itemView.tvManufacture.setTextHtml(stringRes(R.string.manufacture, productLocale))

        aggregation?.let { itemView.tvProductPrice.text = stringRes(R.string.price, it.minPrice) } ?: run {
            itemView.tvProductPrice.gone()
        }
        itemView.tvPricePrefix.visibleOrGone(aggregation != null)
        itemView.tvPriceUnavailable.visibleOrGone(aggregation == null)
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductListViewHolder(parent.inflate(R.layout.item_product_search))
    }
}