package com.pulse.manager.data.local

import android.content.Context
import androidx.room.*
import com.pulse.manager.components.category.model.Category
import com.pulse.manager.components.category.model.CategoryDAO
import com.pulse.manager.components.chat.model.message.MessageDAO
import com.pulse.manager.components.chat.model.message.MessageItem
import com.pulse.manager.components.chat.model.remoteKeys.MessagesRemoteKeys
import com.pulse.manager.components.chat.model.remoteKeys.MessagesRemoteKeysDAO
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.components.chat_list.model.chat.ChatItemDAO
import com.pulse.manager.components.chat_list.model.remoteKeys.ChatsRemoteKeys
import com.pulse.manager.components.chat_list.model.remoteKeys.ChatsRemoteKeysDAO
import com.pulse.manager.components.product.model.Picture
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.product.model.RecentlyRecommendedDAO
import com.pulse.manager.components.signIn.model.User
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.core.general.interfaces.ManagerInterface
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

    val categoryDAO
        get() = db.categoryDAO()

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
            ProductLite::class,
            Category::class
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

        abstract fun categoryDAO(): CategoryDAO
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