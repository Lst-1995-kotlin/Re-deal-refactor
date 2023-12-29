package com.hifi.redeal.memo.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.redeal.CurrentUserClass
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class PhotoMemoRepository @Inject constructor(
    private val currentUser: CurrentUserClass
) {
    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val userIdx = currentUser.userIdx
    private val userDataCollection = db.collection("userData")
    private val photoMemoDataCollection =
        userDataCollection.document(userIdx).collection("photoMemoData")

    fun getPhotoMemoAll(clientIdx: Long, callback1: (QuerySnapshot) -> Unit) {
        val photoMemoRef = photoMemoDataCollection.whereEqualTo("clientIdx", clientIdx)
        photoMemoRef.get()
            .addOnSuccessListener(callback1)
    }

    fun addPhotoMemo(
        clientIdx: Long,
        photoMemoContext: String,
        uriList: MutableList<Uri>,
        callback: (Task<Void>) -> Unit
    ) {
        val photoMemoRef = photoMemoDataCollection
        photoMemoRef
            .orderBy("photoMemoIdx", Query.Direction.DESCENDING)
            .limit(1)
            .get().addOnSuccessListener { querySnapshot ->
                val photoMemoIdx = if (!querySnapshot.isEmpty) {
                    querySnapshot.documents[0].getLong("photoMemoIdx")!! + 1
                } else {
                    1
                }
                var uploadCnt = 0
                val fileNameList = mutableListOf<String>()
                for (i in 0 until uriList.size) {
                    val fileName =
                        "image_user${currentUser.userIdx}_client${clientIdx}_photoMemo${photoMemoIdx}_$i"
                    uploadImage(uriList[i], fileName) { isSuccessful ->
                        uploadCnt++
                        if (isSuccessful) {
                            fileNameList.add(fileName)
                            if (uploadCnt == uriList.size) {
                                val photoMemo = hashMapOf(
                                    "clientIdx" to clientIdx,
                                    "photoMemoContext" to photoMemoContext,
                                    "photoMemoSrcArr" to fileNameList,
                                    "photoMemoDate" to Timestamp(Date()),
                                    "photoMemoIdx" to photoMemoIdx
                                )
                                photoMemoRef.document("$photoMemoIdx").set(photoMemo)
                                    .addOnCompleteListener(callback)
                            }
                        }
                    }
                }
            }
    }

    fun getPhotoMemoImgUrl(filename: String, callback: (String) -> Unit) {
        val fileRef = storage.reference.child("user${currentUser.userIdx}/$filename")
        fileRef.downloadUrl.addOnCompleteListener {
            callback(it.result.toString())
        }
    }

    suspend fun getPhotoMemoImgUrlToCoroutine(filename: String): String {
        val fileRef = storage.reference.child("user${currentUser.userIdx}/$filename")

        return try {
            val downloadUrl = fileRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun uploadImage(uploadUri: Uri, fileName: String, callback: (Boolean) -> Unit) {
        val imageRef = storage.reference.child("user${currentUser.userIdx}/$fileName")
        imageRef.putFile(uploadUri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }
}