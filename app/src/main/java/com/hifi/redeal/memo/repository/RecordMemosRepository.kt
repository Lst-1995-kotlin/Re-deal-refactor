package com.hifi.redeal.memo.repository

import com.hifi.redeal.memo.model.RecordMemo
import kotlinx.coroutines.flow.Flow

interface RecordMemosRepository {
    fun getRecordMemos(): Flow<List<RecordMemo>>

    fun getClientWithRecordMemos(clientId:Int): Flow<List<RecordMemo>>

    fun getRecordMemo(id:Int): Flow<RecordMemo>

    suspend fun insertRecordMemo(recordMemo: RecordMemo)

    suspend fun updateRecordMemo(recordMemo: RecordMemo)

    suspend fun deleteRecordMemo(recordMemo: RecordMemo)
}