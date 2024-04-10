package com.hifi.redeal.memo.repository

import com.hifi.redeal.data.dao.RecordMemoDao
import com.hifi.redeal.data.entrie.RecordMemoEntity
import com.hifi.redeal.data.entrie.asExternalModel
import com.hifi.redeal.memo.model.RecordMemo
import com.hifi.redeal.memo.model.toRecordMemoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineRecordMemosRepository @Inject constructor(
    private val recordMemoDao: RecordMemoDao
) : RecordMemosRepository {
    override fun getRecordMemos(): Flow<List<RecordMemo>> =
        recordMemoDao.getRecordMemoEntities()
            .map { it.map(RecordMemoEntity::asExternalModel) }

    override fun getClientWithRecordMemos(clientId: Int): Flow<List<RecordMemo>> =
        recordMemoDao.getClientWithRecordMemoEntities(clientId)
            .map { it.map(RecordMemoEntity::asExternalModel) }

    override fun getRecordMemo(id: Int): Flow<RecordMemo> =
        recordMemoDao.getRecordMemoEntity(id)
            .map(RecordMemoEntity::asExternalModel)

    override suspend fun insertRecordMemo(recordMemo: RecordMemo) =
        recordMemoDao.insertRecordMemo(recordMemo.toRecordMemoEntity())

    override suspend fun updateRecordMemo(recordMemo: RecordMemo) =
        recordMemoDao.updateRecordMemo(recordMemo.toRecordMemoEntity())

    override suspend fun deleteRecordMemo(recordMemo: RecordMemo) =
        recordMemoDao.deleteRecordMemo(recordMemo.toRecordMemoEntity())
}