package com.pharmacy.manager.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.CornerFamily
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.model.ChatMessage
import com.pharmacy.manager.core.adapter.BaseViewHolder
import com.pharmacy.manager.core.extensions.inflate
import com.pharmacy.manager.core.extensions.load
import com.pharmacy.manager.core.extensions.resources
import com.pharmacy.manager.core.extensions.setTextHtml
import kotlinx.android.synthetic.main.item_chat_product.view.*

class ProductViewHolder(itemView: View) : BaseViewHolder<ChatMessage>(itemView) {

    init {
        val radius = resources.getDimension(R.dimen._8sdp)
        itemView.cardChatProduct.shapeAppearanceModel = itemView.cardChatProduct.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()
    }

    override fun bind(item: ChatMessage) {
        with(itemView) {
            val productMessage = item.asProduct()
            tvChatProductRecipe.text = "Рецепт" // TODO
            tvChatProductDescription.text = productMessage.product.description
            productMessage.product.aggregation?.let {
                tvChatProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
            }
            tvChatProductTitle.setTextHtml(productMessage.product.rusName)
            productMessage.product.pictures.firstOrNull()?.let {
                ivChatProduct.load(it.url)
            }
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductViewHolder(parent.inflate(R.layout.item_chat_product))
    }
}