package com.hifi.redeal.memo.repository

import com.hifi.redeal.data.dao.PhotoMemoDao
import com.hifi.redeal.data.entrie.PhotoMemoEntity
import com.hifi.redeal.data.entrie.asExternalModel
import com.hifi.redeal.memo.model.PhotoMemo
import com.hifi.redeal.memo.model.toPhotoMemoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflinePhotoMemosRepository @Inject constructor(
    private val photoMemoDao: PhotoMemoDao
) : PhotoMemosRepository{
    override fun getPhotoMemos(): Flow<List<PhotoMemo>> =
        photoMemoDao.getPhotoMemoEntities()
            .map { it.map(PhotoMemoEntity::asExternalModel) }

    override fun getClientWithPhotoMemos(clientId:Int): Flow<List<PhotoMemo>> =
        photoMemoDao.getClientWithPhotoMemoEntities(clientId)
            .map { it.map(PhotoMemoEntity::asExternalModel) }
    override fun getPhotoMemo(id:Int): Flow<PhotoMemo> =
        photoMemoDao.getPhotoMemoEntity(id).map {it.asExternalModel()}

    override suspend fun insertPhotoMemo(photoMemo: PhotoMemo) =
        photoMemoDao.insertPhotoMemo(photoMemo.toPhotoMemoEntity())

    override suspend fun updatePhotoMemo(photoMemo: PhotoMemo) =
        photoMemoDao.updatePhotoMemo(photoMemo.toPhotoMemoEntity())

    override suspend fun deletePhotoMemo(photoMemo: PhotoMemo) =
        photoMemoDao.deletePhotoMemo(photoMemo.toPhotoMemoEntity())
}