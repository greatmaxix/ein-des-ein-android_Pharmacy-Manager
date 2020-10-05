package com.pharmacy.manager.components.home.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.manager.R
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import com.pharmacy.manager.core.extensions.load
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener
import com.pharmacy.manager.core.extensions.setTextHtml
import kotlinx.android.synthetic.main.item_product_short.view.*

class ProductAdapter(private val itemClick: (Product) -> Unit) : BaseFilterRecyclerAdapter<Product, BaseViewHolder<Product>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder.newInstance(parent, itemClick)

    override fun diffResult(origin: List<Product>, new: List<Product>) = ProductDiff(origin, new)

    class ProductViewHolder(itemView: View, itemClick: (Product) -> Unit) : BaseViewHolder<Product>(itemView) {

        init {
            itemView.setDebounceOnClickListener {
                itemClick.invoke(tag as Product)
            }
        }

        override fun bind(item: Product) {
            itemView.tag = item
            with(itemView) {
                tvRecipe.text = "Рецепт" // TODO
                tvProductDescription.text = item.description
                item.aggregation?.let {
                    tvProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
                }
                tvProductTitle.setTextHtml(item.rusName)
                item.pictures.firstOrNull()?.url?.let(ivProductImage::load)
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup, itemClick: (Product) -> Unit) = ProductViewHolder(parent.inflate(R.layout.item_product_short), itemClick)
        }
    }
}