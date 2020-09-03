package com.pharmacy.manager.data.local

import android.content.Context
import com.pharmacy.manager.core.general.interfaces.ManagerInterface

class DBManager(context: Context) : ManagerInterface {

    companion object {
        private const val NAME = "sample"
        private const val VERSION = 1
    }
// TODO implement
//    private val db = Room.databaseBuilder(context, LocalDB::class.java, NAME)
//        .apply { fallbackToDestructiveMigration() }
//        .build()

    override fun clear() {

    }
}