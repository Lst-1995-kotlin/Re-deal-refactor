package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.TradeEntity
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.model.TradeSelectData
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
           trade.client_id AS clientId,
           client.name AS clientName,
           client.manager_name AS managerName
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
           client.name AS clientName,
           client.manager_name AS managerName
    FROM trade 
    INNER JOIN client ON trade.client_id = client.id
    ORDER BY date DESC
"""
    )
    fun getAllSelectTrade(): Flow<List<TradeSelectData>>
    @Query(
        """
    SELECT trade.id AS id, 
           trade.item_name AS itemName, 
           trade.item_count AS itemCount,
           trade.item_price AS itemPrice,
           trade.received_amount AS receivedAmount,
           trade.type AS type,
           trade.date AS date,
           trade.client_id AS clientId,
           client.name AS clientName,
           client.manager_name AS managerName
    FROM trade 
    INNER JOIN client ON trade.client_id = client.id
    WHERE trade.client_id = :clientId
    ORDER BY date DESC
"""
    )
    fun getTradeByClient(clientId: Int): Flow<List<TradeData>>

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
           client.name AS clientName,
           client.manager_name AS managerName
    FROM trade 
    INNER JOIN client ON trade.client_id = client.id
    WHERE trade.client_id = :clientId
    ORDER BY date DESC
"""
    )
    fun getSelectTradeByClient(clientId: Int): Flow<List<TradeSelectData>>

    @Query(
        """
            SELECT trade.id AS id, 
           trade.item_name AS itemName, 
           trade.item_count AS itemCount,
           trade.item_price AS itemPrice,
           trade.received_amount AS receivedAmount,
           trade.type AS type,
           trade.date AS date,
           trade.client_id AS clientId,
           client.name AS clientName,
           client.manager_name AS managerName
    FROM trade  
    INNER JOIN client ON trade.client_id = client.id
    WHERE trade.id = :tradeId
        """
    )
    fun getTradeById(tradeId: Int): Flow<TradeData>

    @Query(
        """
            UPDATE trade
            SET checked = false
            WHERE checked = true;
        """
    )
    suspend fun selectHistoryClear()

    @Insert
    suspend fun insertTrade(tradeEntity: TradeEntity)

    @Update
    suspend fun updateTrade(tradeEntity: TradeEntity)

    @Delete
    suspend fun deleteTrade(tradeEntity: TradeEntity)
}