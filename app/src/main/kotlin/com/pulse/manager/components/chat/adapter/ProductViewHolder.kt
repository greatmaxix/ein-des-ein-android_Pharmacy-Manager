package com.pulse.manager.components.chat.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.shape.CornerFamily
import com.pulse.manager.R
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.core.adapter.BaseViewHolder
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.ItemChatProductBinding

class ProductViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatProductBinding::bind)

    init {
        val radius = resources.getDimension(R.dimen._8sdp)
        binding.mcvChatProduct.shapeAppearanceModel = binding.mcvChatProduct.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()
    }

    override fun bind(item: MessageItem) {
        with(binding) {
//            tvChatProductRecipe.text = "Рецепт" // TODO set value and make visible
            mtvDescription.setTextHtml(item.product?.releaseForm)
            item.product?.pharmacyProductsAggregationData?.let {
                mtvProductPrice.text = context.getString(R.string.price, it.minPrice.toString())
            }
            mtvTitle.setTextHtml(item.product?.rusName)
            item.product?.let(ivProduct::setProductImage)
        }
    }

    companion object {

        fun newInstance(parent: ViewGroup) = ProductViewHolder(parent.inflate(R.layout.item_chat_product))
    }
}