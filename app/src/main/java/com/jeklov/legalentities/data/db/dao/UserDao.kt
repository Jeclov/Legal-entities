package com.jeklov.legalentities.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeklov.legalentities.data.models.User

@Dao
interface UserDao {
    // User DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserItem(userItem: User)

    @Delete
    suspend fun deleteUserItem(userItem: User)

    @Query("SELECT * FROM userItems")
    fun getAllUserItems() : LiveData<List<User>>

    @Query("SELECT * FROM userItems WHERE id = :id")
    fun getUserByIdItems(id: Int) : LiveData<User?>
}