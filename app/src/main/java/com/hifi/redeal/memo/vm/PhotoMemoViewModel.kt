package com.hifi.redeal.memo.vm

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.memo.model.PhotoMemoData
import com.hifi.redeal.memo.repository.PhotoMemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoMemoViewModel @Inject constructor(
    private val photoMemoRepository: PhotoMemoRepository,
) : ViewModel() {
    private val _photoMemoList = MutableLiveData<List<PhotoMemoData>>()
    val photoMemoList: LiveData<List<PhotoMemoData>> get() = _photoMemoList

    fun getPhotoMemoList(clientIdx: Long) {
        photoMemoRepository.getPhotoMemoAll(clientIdx) { documentSnapshot ->
            val updatedList = mutableListOf<PhotoMemoData>()
            for (item in documentSnapshot) {
                val context = item.get("photoMemoContext") as String
                val date = item.get("photoMemoDate") as Timestamp
                val srcArr = item.get("photoMemoSrcArr") as List<String>
                val newPhotoMemo = PhotoMemoData(context, date, srcArr, clientIdx)
                updatedList.add(newPhotoMemo)
            }
            updatedList.reverse()
            _photoMemoList.value = updatedList
        }
    }
}