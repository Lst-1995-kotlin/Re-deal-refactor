package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.data.entrie.PhotoMemoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoMemoDao {
    @Query("SELECT * FROM photoMemos ORDER BY timestamp DESC")
    fun getPhotoMemoEntities(): Flow<List<PhotoMemoEntity>>
    @Query("Select * FROM photoMemos WHERE clientOwnerId = :clientId ORDER BY timestamp DESC")
    fun getClientWithPhotoMemoEntities(clientId:Int): Flow<List<PhotoMemoEntity>>
    @Query("SELECT * FROM photoMemos WHERE id = :id")
    fun getPhotoMemoEntity(id: Int): Flow<PhotoMemoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhotoMemo(photoMemo: PhotoMemoEntity)

    @Update
    suspend fun updatePhotoMemo(photoMemo: PhotoMemoEntity)

    @Delete
    suspend fun deletePhotoMemo(photoMemo: PhotoMemoEntity)
}