package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.TestEntry

@Dao
interface TestDao {
    @Query("SELECT * FROM test")
    fun getAllTest(): List<TestEntry>

    @Insert
    fun insertUser(user: TestEntry)

    @Update
    suspend fun updateUser(user: TestEntry)

    @Delete
    suspend fun deleteUser(user: TestEntry)
}