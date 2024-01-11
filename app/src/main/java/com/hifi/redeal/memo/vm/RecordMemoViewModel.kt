package com.hifi.redeal.memo.vm

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.memo.model.RecordMemoData
import com.hifi.redeal.memo.repository.RecordMemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordMemoViewModel @Inject constructor(
    private val recordMemoRepository: RecordMemoRepository
) : ViewModel() {
    val recordMemoList: LiveData<List<RecordMemoData>> get() = _recordMemoList
    private val _recordMemoList = MutableLiveData<List<RecordMemoData>>()
    fun getRecordMemoList(clientIdx:Long, mainContext:Context){
        recordMemoRepository.getRecordMemoAll(clientIdx){ documentSnapshot ->
            val recordMemoData = mutableListOf<RecordMemoData>()
            for(item in documentSnapshot){
                val context = item.get("recordMemoContext") as String
                val date = item.get("recordMemoDate") as Timestamp
                val audioFilename = item.get("recordMemoFilename") as String
                val fileLocation = File(mainContext.getExternalFilesDir(null), "recordings")
                val recordFileLocation = File(fileLocation, audioFilename)
                var audioFileUri:Uri? = null
                if(recordFileLocation.exists()){
                    audioFileUri = Uri.fromFile(recordFileLocation)
                    val newRecordMemo = RecordMemoData(context, date, audioFileUri, audioFilename)
                    recordMemoData.add(newRecordMemo)
                }
            }
            recordMemoData.reverse()
            _recordMemoList.value = recordMemoData
        }
    }
}