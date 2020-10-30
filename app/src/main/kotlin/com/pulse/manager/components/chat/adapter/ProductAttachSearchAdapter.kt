package com.pulse.manager.components.chat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pulse.manager.R
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.core.extensions.load
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.core.extensions.setTextHtml
import kotlinx.android.synthetic.main.item_product_short.view.*

class ProductAttachSearchAdapter(private val itemClick: (ProductLite) -> Unit) : PagingDataAdapter<ProductLite, BaseViewHolder<ProductLite>>(ProductAttachSearchDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder.newInstance(parent, itemClick)

    override fun onBindViewHolder(holder: BaseViewHolder<ProductLite>, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    class ProductViewHolder(itemView: View, itemClick: (ProductLite) -> Unit) : BaseViewHolder<ProductLite>(itemView) {

        init {
            itemView.setDebounceOnClickListener {
                itemClick.invoke(tag as ProductLite)
            }
        }

        override fun bind(item: ProductLite) {
            itemView.tag = item
            with(itemView) {
//                tvRecipe.text = "Рецепт" // TODO
                tvProductDescription.text = item.releaseForm
                item.aggregation?.let {
                    tvProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
                }
                tvProductTitle.setTextHtml(item.rusName)
                item.pictures.firstOrNull()?.url?.let(ivProductImage::load)
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup, itemClick: (ProductLite) -> Unit) = ProductViewHolder(parent.inflate(R.layout.item_product_short), itemClick)
        }
    }
}