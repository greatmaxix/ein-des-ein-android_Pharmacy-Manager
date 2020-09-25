package com.pharmacy.manager.components.chat.model

import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_ATTACHMENT
import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_DATE_HEADER
import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_MESSAGE_PHARMACY
import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_MESSAGE_USER
import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter.Companion.TYPE_PRODUCT
import com.pharmacy.manager.components.product.model.Product
import java.time.LocalDateTime

sealed class ChatMessage(val itemType: Int) {

    class PharmacyMessage(
        val message: String,
        val readDate: LocalDateTime? = null
    ) : ChatMessage(TYPE_MESSAGE_USER)

    fun asUserMessage() = this as PharmacyMessage

    class UserMessage(
        val message: String
    ) : ChatMessage(TYPE_MESSAGE_PHARMACY)

    fun asPharmacyMessage() = this as UserMessage

    class DateHeader(
        val date: LocalDateTime
    ) : ChatMessage(TYPE_DATE_HEADER)

    fun asDateHeader() = this as DateHeader

    class Attachment(
        val items: MutableList<String> // TODO change type of content
    ) : ChatMessage(TYPE_ATTACHMENT)

    fun asAttachment() = this as Attachment

    class ChatProduct(
        val product: Product
    ) : ChatMessage(TYPE_PRODUCT)

    fun asProduct() = this as ChatProduct
}