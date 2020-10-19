package com.pharmacy.manager.data.local

import android.content.Context
import androidx.room.*
import com.pharmacy.manager.components.chat.model.message.MessageDAO
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chat.model.remoteKeys.MessagesRemoteKeys
import com.pharmacy.manager.components.chat.model.remoteKeys.MessagesRemoteKeysDAO
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.components.chatList.model.chat.ChatItemDAO
import com.pharmacy.manager.components.chatList.model.remoteKeys.ChatsRemoteKeys
import com.pharmacy.manager.components.chatList.model.remoteKeys.ChatsRemoteKeysDAO
import com.pharmacy.manager.components.product.model.Picture
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.product.model.RecentlyRecommendedDAO
import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.core.general.interfaces.ManagerInterface
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DBManager(context: Context) : ManagerInterface {

    companion object {
        private const val NAME = "pharmacy_manager_db"
        private const val VERSION = 1
    }

    private val db = Room.databaseBuilder(context, LocalDB::class.java, NAME)
        .apply {
            fallbackToDestructiveMigration()
        }
        .build()

    val userDAO
        get() = db.userDAO()

    val messageDAO
        get() = db.messageDAO()

    val messageRemoteKeysDAO
        get() = db.messageRemoteKeysDAO()

    val chatItemDAO
        get() = db.chatItemDAO()

    val chatsRemoteKeysDAO
        get() = db.chatsRemoteKeysDAO()

    val recentlyViewedDAO
        get() = db.recentlyViewedDAO()

    override fun clear() {
        recentlyViewedDAO.clear()
    }

    @Database(
        entities = [
            User::class,
            MessageItem::class,
            MessagesRemoteKeys::class,
            ChatItem::class,
            ChatsRemoteKeys::class,
            ProductLite::class
        ], version = VERSION, exportSchema = false
    )
    @TypeConverters(
        StringListConverter::class,
        PicturesListConverter::class,
        LocalDateTimeConverter::class
    )
    abstract class LocalDB : RoomDatabase() {

        abstract fun userDAO(): UserDAO

        abstract fun messageDAO(): MessageDAO

        abstract fun messageRemoteKeysDAO(): MessagesRemoteKeysDAO

        abstract fun chatItemDAO(): ChatItemDAO

        abstract fun chatsRemoteKeysDAO(): ChatsRemoteKeysDAO

        abstract fun recentlyViewedDAO(): RecentlyRecommendedDAO
    }

    class StringListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }

        @TypeConverter
        fun fromList(list: List<String>) = list.joinToString("|")
    }

    class PicturesListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }.map { Picture(it) }

        @TypeConverter
        fun fromList(list: List<Picture>) = list.joinToString("|") { it.url }
    }

    class LocalDateTimeConverter {
        @TypeConverter
        fun toLocalDateTime(value: Long?) = value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }

        @TypeConverter
        fun fromLocalDateTime(localDateTime: LocalDateTime) = localDateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}