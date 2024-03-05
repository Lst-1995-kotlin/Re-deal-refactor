package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.ClientEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM client")
    fun getAllClient(): Flow<List<ClientEntry>>

    @Insert
    suspend fun insertClient(client: ClientEntry)

    @Update
    suspend fun updateClient(client: ClientEntry)

    @Delete
    suspend fun deleteClient(client: ClientEntry)
}