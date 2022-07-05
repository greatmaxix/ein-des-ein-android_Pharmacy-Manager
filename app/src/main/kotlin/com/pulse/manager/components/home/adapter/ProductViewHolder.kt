package com.pulse.manager.components.home.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.context
import com.pulse.manager.core.extensions.inflate
import com.pulse.manager.core.extensions.setProductImage
import com.pulse.manager.core.extensions.setTextHtml
import com.pulse.manager.databinding.ItemProductShortBinding

class ProductViewHolder(itemView: View) : BaseViewHolder<ProductLite>(itemView) {

    private val binding by viewBinding(ItemProductShortBinding::bind)

    override fun bind(item: ProductLite) {
        with(binding) {
//                tvRecipe.text = "Рецепт" // TODO
            item.aggregation?.let { mtvProductPrice.text = context.getString(R.string.price, it.minPrice.toString()) }
            mtvTitle.setTextHtml(item.rusName)
            mtvSubtitle.setTextHtml(item.releaseForm)
            ivProduct.setProductImage(item)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductViewHolder(parent.inflate(R.layout.item_product_short))
    }
}