package com.pharmacy.manager.data.local.db

import androidx.annotation.WorkerThread
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Update

interface BaseDao<T> {

    @WorkerThread
    @Insert(onConflict = REPLACE)
    fun insert(obj: T)

    @WorkerThread
    @Insert(onConflict = REPLACE)
    fun insert(obj: List<T>)

    @WorkerThread
    @Insert(onConflict = REPLACE)
    fun insert(vararg obj: T)

    @WorkerThread
    @Update(onConflict = REPLACE)
    fun update(obj: T)

    @WorkerThread
    @Delete
    fun delete(obj: T)
}