package com.pharmacy.manager.components.home.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener
import com.pharmacy.manager.core.extensions.setProductImage
import com.pharmacy.manager.core.extensions.setTextHtml
import kotlinx.android.synthetic.main.item_product_short.view.*

class ProductAdapter(private val itemClick: (ProductLite) -> Unit) : BaseFilterRecyclerAdapter<ProductLite, BaseViewHolder<ProductLite>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder.newInstance(parent, itemClick)

    override fun diffResult(origin: List<ProductLite>, new: List<ProductLite>) = ProductDiff(origin, new)

    class ProductViewHolder(itemView: View, itemClick: (ProductLite) -> Unit) : BaseViewHolder<ProductLite>(itemView) {

        init {
            itemView.setDebounceOnClickListener {
                itemClick.invoke(tag as ProductLite)
            }
        }

        override fun bind(item: ProductLite) {
            itemView.tag = item
            with(itemView) {
                tvRecipe.text = "Рецепт" // TODO
                tvProductDescription.text = item.releaseForm
                item.aggregation?.let {
                    tvProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
                }
                tvProductTitle.setTextHtml(item.rusName)
                ivProductImage.setProductImage(item)
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup, itemClick: (ProductLite) -> Unit) = ProductViewHolder(parent.inflate(R.layout.item_product_short), itemClick)
        }
    }
}