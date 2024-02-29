package com.hifi.redeal.memo.vm

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.memo.model.RecordMemoData
import com.hifi.redeal.memo.repository.RecordMemoRepository
import com.hifi.redeal.memo.utils.getUriForFile
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordMemoViewModel @Inject constructor(
    private val recordMemoRepository: RecordMemoRepository
) : ViewModel() {
    val recordMemoList: LiveData<List<RecordMemoData>> get() = _recordMemoList
    private val _recordMemoList = MutableLiveData<List<RecordMemoData>>()
    fun getRecordMemoList(clientIdx: Long, mainContext: Context) {
        recordMemoRepository.getRecordMemoAll(clientIdx) { documentSnapshot ->
            val recordMemoData = mutableListOf<RecordMemoData>()
            for (item in documentSnapshot) {
                val context = item.get("recordMemoContext") as String
                val date = item.get("recordMemoDate") as Timestamp
                val audioFilename = item.get("recordMemoFilename") as String
                val duration = item.get("recordMemoDuration") as Long
                val audioFileUri = Uri.parse(item.get("recordMemoSrc") as String)
                val newRecordMemo = RecordMemoData(
                    context,
                    date.toDate(),
                    audioFileUri,
                    audioFilename,
                    duration
                )
                recordMemoData.add(newRecordMemo)
            }
            _recordMemoList.value = recordMemoData.sortedByDescending { it.date }
        }
    }
}