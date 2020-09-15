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
            tvChatProductRecipe.text = productMessage.product.recipeTitle
            tvChatProductDescription.text = productMessage.product.description
            tvChatProductPrice.text = productMessage.product.price
            tvChatProductTitle.text = productMessage.product.name
            ivChatProduct.load(productMessage.product.imageUrl)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductViewHolder(parent.inflate(R.layout.item_chat_product))
    }
}