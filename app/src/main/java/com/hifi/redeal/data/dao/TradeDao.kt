package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.TradeEntry
import com.hifi.redeal.trade.data.model.TradeData
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Query(
        """
    SELECT trade.id AS id, 
           trade.item_name AS itemName, 
           trade.item_count AS itemCount,
           trade.item_price AS itemPrice,
           trade.received_amount AS receivedAmount,
           trade.type AS type,
           trade.date AS date,
           trade.checked AS checked,
           trade.client_id AS clientId,
           client.name AS clientName
    FROM trade 
    INNER JOIN client ON trade.client_id = client.id
    ORDER BY date DESC
"""
    )
    fun getAllTrade(): Flow<List<TradeData>>

    @Query(
        """
    SELECT trade.id AS id, 
           trade.item_name AS itemName, 
           trade.item_count AS itemCount,
           trade.item_price AS itemPrice,
           trade.received_amount AS receivedAmount,
           trade.type AS type,
           trade.date AS date,
           trade.checked AS checked,
           trade.client_id AS clientId,
           client.name AS clientName
    FROM trade 
    INNER JOIN client ON trade.client_id = client.id
    WHERE trade.client_id = :clientId
    ORDER BY date DESC
"""
    )
    fun getClientTrade(clientId: Int): Flow<List<TradeData>>

    @Insert
    suspend fun insertTrade(tradeEntry: TradeEntry)

    @Update
    suspend fun updateTrade(tradeEntry: TradeEntry)

    @Delete
    suspend fun deleteTrade(tradeEntry: TradeEntry)
}