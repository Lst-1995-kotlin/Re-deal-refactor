package com.hifi.redeal.data.entrie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("test")
data class TestEntry(
    @PrimaryKey val uid: Int,
    @ColumnInfo val firstName: String
)

@Entity("test2")
data class Test2Entry(
    @PrimaryKey val uid: Int,
    @ColumnInfo val firstName: String
)