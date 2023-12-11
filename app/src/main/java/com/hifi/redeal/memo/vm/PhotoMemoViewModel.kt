package com.hifi.redeal.memo.vm

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
    val photoMemoList = MutableLiveData<List<PhotoMemoData>>()

    init {
        photoMemoList.value = listOf<PhotoMemoData>()
    }

    fun getPhotoMemoList(clientIdx: Long) {
        photoMemoRepository.getPhotoMemoAll(clientIdx) { documentSnapshot ->
            val photoMemoData = mutableListOf<PhotoMemoData>()
            for (item in documentSnapshot) {
                val context = item.get("photoMemoContext") as String
                val date = item.get("photoMemoDate") as Timestamp
                val srcArr = item.get("photoMemoSrcArr") as List<String>
                val newPhotoMemo = PhotoMemoData(context, date, srcArr, clientIdx)
                photoMemoData.add(newPhotoMemo)
            }
            photoMemoData.reverse()
            photoMemoList.postValue(photoMemoData)
        }
    }
}
