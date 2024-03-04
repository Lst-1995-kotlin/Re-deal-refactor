package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.Test2Entry

@Dao
interface TestDao2 {

    @Query("SELECT * FROM test2")
    fun getAllTest(): List<Test2Entry>

    @Insert
    fun insertUser(user: Test2Entry)

    @Update
    fun updateUser(user: Test2Entry)

    @Delete
    fun deleteUser(user: Test2Entry)
}