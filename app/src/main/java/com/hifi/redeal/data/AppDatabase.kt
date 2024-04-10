package com.hifi.redeal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hifi.redeal.data.dao.ClientDao
import com.hifi.redeal.data.dao.PhotoMemoDao
import com.hifi.redeal.data.dao.RecordMemoDao
import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.data.entrie.ClientEntry
import com.hifi.redeal.data.entrie.PhotoMemoEntity
import com.hifi.redeal.data.entrie.RecordMemoEntity
import com.hifi.redeal.data.entrie.TradeEntity

@Database(
    entities = [
        ClientEntry::class,
        TradeEntry::class,
        PhotoMemoEntity::class,
        RecordMemoEntity::class,
    ],
    version = 1
)
@TypeConverters(DateConverters::class, StringListToJsonConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun tradeDao(): TradeDao
    abstract fun photoMemoDao(): PhotoMemoDao
    abstract fun recordMemoDao(): RecordMemoDao
}