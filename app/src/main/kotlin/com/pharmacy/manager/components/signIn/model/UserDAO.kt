package com.pharmacy.manager.components.signIn.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {

    @Query("select * from user limit 1")
    fun get(): LiveData<User>

    @Query("select * from user limit 1")
    suspend fun getUser(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

    suspend fun isUserExist() = getUser() != null

    @Query("DELETE FROM user")
    fun clear()
}