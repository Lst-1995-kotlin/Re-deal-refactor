package com.hifi.redeal.memo.repository
import com.hifi.redeal.memo.model.PhotoMemo
import kotlinx.coroutines.flow.Flow

interface PhotoMemosRepository {
    fun getPhotoMemos(): Flow<List<PhotoMemo>>

    fun getClientWithPhotoMemos(clientId:Int): Flow<List<PhotoMemo>>

    fun getPhotoMemo(id:Int):Flow<PhotoMemo>

    suspend fun insertPhotoMemo(photoMemo: PhotoMemo)

    suspend fun updatePhotoMemo(photoMemo: PhotoMemo)

    suspend fun deletePhotoMemo(photoMemo: PhotoMemo)
}