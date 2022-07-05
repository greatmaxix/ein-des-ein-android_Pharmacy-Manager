package com.pulse.manager.components.search.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.ItemProductSearchBinding
import kotlinx.coroutines.FlowPreview
import org.koin.core.KoinExperimentalAPI

class ProductListViewHolder(override val containerView: View) : BaseViewHolder<ProductLite>(containerView) {

    private val binding by viewBinding(ItemProductSearchBinding::bind)

    @KoinExperimentalAPI
    @FlowPreview
    @ExperimentalPagingApi
    override fun bind(item: ProductLite) = with(item) {
        with(binding) {
            ivProduct.setProductImage(item)
            mtvTitle.setTextHtml(rusName)
            mtvSubtitle.setTextHtml(releaseForm)
            mtvManufacture.setTextHtml(stringRes(R.string.manufacture, productLocale))
            aggregation?.let { mtvProductPrice.text = stringRes(R.string.price, it.minPrice) } ?: run { mtvProductPrice.gone() }
            mtvPricePrefix.visibleOrGone(aggregation != null)
            mtvPriceUnavailable.visibleOrGone(aggregation == null)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductListViewHolder(parent.inflate(R.layout.item_product_search))
    }
}