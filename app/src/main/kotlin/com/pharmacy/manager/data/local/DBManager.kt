package com.pharmacy.manager.data.local

import android.content.Context
import androidx.room.*
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

    override fun clear() {

    }

    @Database(entities = [User::class], version = VERSION, exportSchema = false)
    @TypeConverters(StringListConverter::class)
    abstract class LocalDB : RoomDatabase() {

        abstract fun userDAO(): UserDAO
    }

    class StringListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }

        @TypeConverter
        fun fromList(list: List<String>) = list.joinToString("|")
    }
}