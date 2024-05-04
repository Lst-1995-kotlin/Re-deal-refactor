package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.ClientEntity
import com.hifi.redeal.trade.data.model.TradeClientData
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM client")
    fun getAllClient(): Flow<List<ClientEntity>>

    @Query(
        """
        SELECT id,
        name,
        manager_name AS managerName,
        state,
        bookmark        
        FROM client
        ORDER BY id ASC
    """
    )
    fun getClientTradeData(): Flow<List<TradeClientData>>

    @Query(
        """
        SELECT id,
        name,
        manager_name AS managerName,
        state,
        bookmark        
        FROM client
        WHERE id = :id
    """
    )
    fun getClientById(id: Int): Flow<TradeClientData>

    @Insert
    suspend fun insertClient(client: ClientEntity)

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Delete
    suspend fun deleteClient(client: ClientEntity)
}