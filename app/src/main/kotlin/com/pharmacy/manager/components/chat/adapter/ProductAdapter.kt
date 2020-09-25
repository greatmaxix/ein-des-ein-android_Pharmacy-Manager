package com.pharmacy.manager.components.chat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.model.TempProduct
import com.pharmacy.manager.core.adapter.BaseFilterRecyclerAdapter
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import com.pharmacy.manager.core.extensions.load
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(private val itemClick: (TempProduct) -> Unit) : BaseFilterRecyclerAdapter<TempProduct, BaseViewHolder<TempProduct>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TempProduct> = ProductViewHolder.newInstance(parent, itemClick)

    override fun diffResult(origin: List<TempProduct>, new: List<TempProduct>): DiffUtil.Callback = ProductDiff(origin, new)

    class ProductViewHolder(itemView: View, itemClick: (TempProduct) -> Unit) : BaseViewHolder<TempProduct>(itemView) {

        init {
            itemView.setDebounceOnClickListener {
                itemClick.invoke(tag as TempProduct)
            }
        }

        override fun bind(item: TempProduct) {
            itemView.tag = item
            with(itemView) {
                tvRecipe.text = item.recipeTitle
                tvProductDescription.text = item.description
                tvProductPrice.text = item.price
                tvProductTitle.text = item.name
                ivProductImage.load(item.imageUrl)
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup, itemClick: (TempProduct) -> Unit) = ProductViewHolder(parent.inflate(R.layout.item_product), itemClick)
        }
    }
}