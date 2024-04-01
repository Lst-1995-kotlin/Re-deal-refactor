package com.hifi.redeal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hifi.redeal.data.dao.ClientDao
import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.data.entrie.ClientEntry
import com.hifi.redeal.data.entrie.TradeEntry

@Database(
    entities = [ClientEntry::class, TradeEntry::class],
    version = 1
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun tradeDao(): TradeDao
}