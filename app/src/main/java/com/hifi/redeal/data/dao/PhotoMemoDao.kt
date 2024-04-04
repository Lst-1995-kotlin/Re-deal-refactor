package com.hifi.redeal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hifi.redeal.memo.model.PhotoMemo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoMemoDao {
    @Query("SELECT * FROM photoMemos ORDER BY timestamp ASC")
    fun getPhotoMemoEntities(): Flow<List<PhotoMemo>>

    @Query("SELECT * FROM photoMemos WHERE id = :id")
    fun getPhotoMemoEntity(id: Int): Flow<PhotoMemo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhotoMemo(photoMemo: PhotoMemo)

    @Update
    suspend fun updatePhotoMemo(photoMemo: PhotoMemo)

    @Delete
    suspend fun deletePhotoMemo(photoMemo: PhotoMemo)
}