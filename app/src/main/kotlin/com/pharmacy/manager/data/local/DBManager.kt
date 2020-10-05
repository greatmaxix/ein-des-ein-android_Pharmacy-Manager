package com.pharmacy.manager.data.local

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.pharmacy.manager.components.chat.model.message.Application
import com.pharmacy.manager.components.chat.model.message.MessageDAO
import com.pharmacy.manager.components.chat.model.message.MessageItem
import com.pharmacy.manager.components.chat.model.remoteKeys.RemoteKeys
import com.pharmacy.manager.components.chat.model.remoteKeys.RemoteKeysDAO
import com.pharmacy.manager.components.product.model.Picture
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.product.model.RecentlyRecommendedDAO
import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.core.general.interfaces.ManagerInterface

class DBManager(context: Context) : ManagerInterface {

    companion object {
        private const val NAME = "pharmacy_manager_db"
        private const val VERSION = 1
    }

    private val db = Room.databaseBuilder(context, LocalDB::class.java, NAME)
        .apply { fallbackToDestructiveMigration() }
        .build()

    val userDAO
        get() = db.userDAO()

    val messageDAO
        get() = db.messageDAO()

    val remoteKeysDAO
        get() = db.remoteKeysDAO()

    val recentlyViewedDAO
        get() = db.recentlyViewedDAO()

    override fun clear() {
        recentlyViewedDAO.clear()
    }

    @Database(entities = [User::class, MessageItem::class, RemoteKeys::class, ProductLite::class], version = VERSION, exportSchema = false)
    @TypeConverters(StringListConverter::class, ApplicationsListConverter::class, PicturesListConverter::class)
    abstract class LocalDB : RoomDatabase() {

        abstract fun userDAO(): UserDAO

        abstract fun messageDAO(): MessageDAO

        abstract fun remoteKeysDAO(): RemoteKeysDAO

        abstract fun recentlyViewedDAO(): RecentlyRecommendedDAO
    }

    class StringListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }

        @TypeConverter
        fun fromList(list: List<String>) = list.joinToString("|")
    }

    class ApplicationsListConverter {

        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }.map { Gson().fromJson(it, Application::class.java) }

        @TypeConverter
        fun fromList(list: List<Application>) = list.joinToString("|") { Gson().toJson(it) }
    }

    class PicturesListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }.map { Picture(it) }

        @TypeConverter
        fun fromList(list: List<Picture>) = list.joinToString("|") { it.url }
    }
}