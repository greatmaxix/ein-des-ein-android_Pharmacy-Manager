package com.pharmacy.manager.components.chat.model.message

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter
import com.pharmacy.manager.components.product.model.Picture
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
@Entity(indices = [Index(value = ["messageId", "chatId"], unique = true)])
class MessageItem(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("itemId") val itemId: Int = 0,
    @ColumnInfo(name = "messageId")
    @SerializedName("id") val id: Int,
    @SerializedName("ownerUuid") val ownerUuid: String,
    @SerializedName("text") val text: String?,
    @ColumnInfo(name = "messageCreatedAt")
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("chatNumber") val messageNumber: Int,
    @SerializedName("chatId") val chatId: Int,
    @Embedded
    @SerializedName("file") val file: Picture? = null,
    @Embedded
    @SerializedName("globalProductCard") val product: MessageProduct? = null,
    @SerializedName("messageType") var messageType: Int? = null,
    @SerializedName("ownerMessage") var ownerMessage: Boolean? = false
) : Parcelable {

    fun updateMessageType(userUuid: String?) {
        if (messageType == ChatMessageAdapter.TYPE_DATE_HEADER) return
        messageType = when {
            file != null -> ChatMessageAdapter.TYPE_ATTACHMENT
            product != null -> ChatMessageAdapter.TYPE_PRODUCT
            ownerUuid == userUuid -> ChatMessageAdapter.TYPE_MESSAGE_USER
            else -> ChatMessageAdapter.TYPE_MESSAGE_PHARMACY
        }
        ownerMessage = ownerUuid == userUuid
    }

    override fun toString(): String {
        return "MessageItem(id=$id, ownerUuid='$ownerUuid', text=$text, messageNumber=$messageNumber, file=${file?.url}, product=${product?.globalProductId})"
    }

    companion object {

        private val headerDateFormatter by lazy { DateTimeFormatter.ofPattern("dd MMMM, EEEE HH:mm") }

        fun getHeaderInstance(nextMessageItem: MessageItem) = MessageItem(
            id = System.currentTimeMillis().toInt() % Integer.MAX_VALUE,
            ownerUuid = "",
            text = headerDateFormatter.format(nextMessageItem.createdAt),
            messageNumber = nextMessageItem.messageNumber,
            createdAt = nextMessageItem.createdAt.withSecond(0),
            chatId = nextMessageItem.chatId,
            messageType = ChatMessageAdapter.TYPE_DATE_HEADER
        )
    }
}