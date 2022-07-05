package com.pulse.manager.components.signIn.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Query("select * from user limit 1")
    fun get(): Flow<User?>

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