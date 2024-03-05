package com.hifi.redeal.data.entrie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("trade")
data class TradeEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "item_name")
    val itemName: String,
    @ColumnInfo("item_count")
    val itemCount: String,
    @ColumnInfo(name = "item_price")
    val itemPrice: String,
    @ColumnInfo(name = "received_amount")
    val receivedAmount: String,
    @ColumnInfo val type: Boolean,
    @ColumnInfo(name = "total_item_amount")
    val totalItemAmount: Long,
    @ColumnInfo val receivables: Long,
    @ColumnInfo val date: Date,
    @ColumnInfo(name = "client_id")
    val clientId: Int,
)