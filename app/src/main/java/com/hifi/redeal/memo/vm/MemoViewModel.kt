//package com.hifi.redeal.memo.vm
//
//import android.content.Context
//import android.net.Uri
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.google.firebase.Timestamp
//import com.hifi.redeal.memo.model.PhotoMemoData
//import com.hifi.redeal.memo.model.UserRecordMemoData
//import com.hifi.redeal.memo.repository.MemoRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import java.io.File
//import javax.inject.Inject
//
//@HiltViewModel
//class MemoViewModel @Inject constructor(
//    private val memoRepository: MemoRepository,
//) : ViewModel() {
//    val userPhotoMemoList = MutableLiveData<List<PhotoMemoData>>()
//    val userRecordMemoList = MutableLiveData<List<UserRecordMemoData>>()
//
//    init {
//        userPhotoMemoList.value = listOf<PhotoMemoData>()
//        userRecordMemoList.value = listOf<UserRecordMemoData>()
//    }
//    fun getUserPhotoMemoList() {
//        memoRepository.getUserPhotoMemoAll { querySnapshot ->
//            val photoMemoData = mutableListOf<PhotoMemoData>()
//            for (document in querySnapshot) {
//                val clientIdx = document.get("clientIdx") as Long
//                val context = document.get("photoMemoContext") as String
//                val date = document.get("photoMemoDate") as Timestamp
//                val srcArr = document.get("photoMemoSrcArr") as List<String>
//                val newPhotoMemo = PhotoMemoData(context, date.toDate(), srcArr, clientIdx)
//                photoMemoData.add(newPhotoMemo)
//            }
//            photoMemoData.reverse()
//            userPhotoMemoList.postValue(photoMemoData)
//        }
//    }
//
//    fun getUserRecordMemoList(mainContext: Context) {
//        memoRepository.getUserRecordMemoAll() { querySnapshot ->
//            val userRecordMemoData = mutableListOf<UserRecordMemoData>()
//            for (document in querySnapshot) {
//                val clientIdx = document.get("clientIdx") as Long
//                val context = document.get("recordMemoContext") as String
//                val date = document.get("recordMemoDate") as Timestamp
//                val audioFilename = document.get("recordMemoFilename") as String
//                val fileLocation = File(mainContext.getExternalFilesDir(null), "recordings")
//                val recordFileLocation = File(fileLocation, "$audioFilename")
//                var audioFileUri: Uri? = null
//                if (recordFileLocation.exists()) {
//                    audioFileUri = Uri.fromFile(recordFileLocation)
//                    val newPhotoMemo = UserRecordMemoData(clientIdx, context, date, audioFileUri, audioFilename)
//                    userRecordMemoData.add(newPhotoMemo)
//                }
//            }
//            userRecordMemoData.reverse()
//            userRecordMemoList.postValue(userRecordMemoData)
//        }
//    }
//}
