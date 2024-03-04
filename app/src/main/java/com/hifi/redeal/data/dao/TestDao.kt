package com.hifi.redeal.data.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.TestEntry

@Dao
interface TestDao {
    @Query("SELECT * FROM test")
    suspend fun getAllTest(): List<TestEntry>

    @Insert
    suspend fun insertUser(user: TestEntry)

    @Update
    suspend fun updateUser(user: TestEntry)

    @Delete
    suspend fun deleteUser(user: TestEntry)
}