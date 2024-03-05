package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.TradeEntry
import com.hifi.redeal.transaction.model.TradeData
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Query(
        """
    SELECT trade.id as id, 
           trade.item_name as itemName, 
           trade.item_count as itemCount,
           trade.item_price as itemPrice,
           trade.received_amount as receivedAmount,
           trade.type as type,
           trade.total_item_amount as totalItemAmount,
           trade.date as date,
           trade.client_id as clientId,
           client.name as clientName,
           client.manager_name as clientManagerName
    FROM trade 
    INNER JOIN client ON trade.client_id = client.id
    ORDER BY id DESC
"""
    )
    fun getAllTrade(): Flow<List<TradeData>>

    @Insert
    suspend fun insertTrade(tradeEntry: TradeEntry)

    @Update
    suspend fun updateTrade(tradeEntry: TradeEntry)

    @Delete
    suspend fun deleteTrade(tradeEntry: TradeEntry)
}