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
    val itemCount: Long,
    @ColumnInfo(name = "item_price")
    val itemPrice: Long,
    @ColumnInfo(name = "received_amount")
    val receivedAmount: Long, //  받은 금액
    @ColumnInfo val type: Boolean,
    @ColumnInfo val date: Date,
    @ColumnInfo val checked: Boolean,
    @ColumnInfo(name = "client_id")
    val clientId: Int
)