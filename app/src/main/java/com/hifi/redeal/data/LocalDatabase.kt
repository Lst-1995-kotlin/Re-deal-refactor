package com.hifi.redeal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hifi.redeal.data.dao.TestDao
import com.hifi.redeal.data.dao.TestDao2
import com.hifi.redeal.data.entrie.Test2Entry
import com.hifi.redeal.data.entrie.TestEntry

@Database(entities = [TestEntry::class, Test2Entry::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun testDao(): TestDao
    abstract fun testDao2(): TestDao2
}