package com.hifi.redeal.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.RecordMemoEntity
import kotlinx.coroutines.flow.Flow


interface RecordMemoDao {
    @Query("SELECT * FROM recordMemos ORDER BY timestamp DESC")
    fun getRecordMemoEntities(): Flow<List<RecordMemoEntity>>

    @Query("Select * FROM recordMemos WHERE clientOwnerId = :clientId ORDER BY timestamp DESC")
    fun getClientWithRecordMemoEntities(clientId:Int): Flow<List<RecordMemoEntity>>
    @Query("SELECT * FROM recordMemos WHERE id = :id")
    fun getRecordMemoEntity(id: Int): Flow<RecordMemoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecordMemo(photoMemo: RecordMemoEntity)

    @Update
    suspend fun updateRecordMemo(photoMemo: RecordMemoEntity)

    @Delete
    suspend fun deleteRecordMemo(photoMemo: RecordMemoEntity)
}