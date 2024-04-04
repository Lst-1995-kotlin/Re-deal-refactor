package com.hifi.redeal.memo.repository

import com.hifi.redeal.data.dao.PhotoMemoDao
import com.hifi.redeal.memo.model.PhotoMemo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflinePhotoMemosRepository @Inject constructor(
    private val photoMemoDao: PhotoMemoDao
) : PhotoMemosRepository{
    override fun getPhotoMemos(): Flow<List<PhotoMemo>> = photoMemoDao.getPhotoMemoEntities()

    override fun getPhotoMemo(id:Int): Flow<PhotoMemo> = photoMemoDao.getPhotoMemoEntity(id)

    override suspend fun insertPhotoMemo(photoMemo: PhotoMemo) = photoMemoDao.insertPhotoMemo(photoMemo)

    override suspend fun updatePhotoMemo(photoMemo: PhotoMemo) = photoMemoDao.updatePhotoMemo(photoMemo)

    override suspend fun deletePhotoMemo(photoMemo: PhotoMemo) = photoMemoDao.deletePhotoMemo(photoMemo)
}