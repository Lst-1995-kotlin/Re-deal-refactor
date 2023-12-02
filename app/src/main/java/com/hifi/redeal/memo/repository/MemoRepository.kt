package com.hifi.redeal.memo.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hifi.redeal.MainActivity
import com.hifi.redeal.memo.CurrentUserClass
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class MemoRepository @Inject constructor(
    private val currentUser:CurrentUserClass
) {
    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val userIdx = currentUser.userIdx
    private val userDataCollection = db.collection("userData")
    private val recordMemoDataCollection = userDataCollection.document(userIdx).collection("recordMemoData")
    private val photoMemoDataCollection = userDataCollection.document(userIdx).collection("photoMemoData")
    private val clientDataCollection = userDataCollection.document(userIdx).collection("clientData")
    fun getUserRecordMemoAll(callback1: (QuerySnapshot) -> Unit){
        val photoMemoRef = recordMemoDataCollection
        photoMemoRef.get()
            .addOnSuccessListener(callback1)
    }

    fun getUserPhotoMemoAll(callback1: (QuerySnapshot) -> Unit){
        val photoMemoRef = photoMemoDataCollection
        photoMemoRef.get().addOnSuccessListener(callback1)
    }

    fun getUserMemoClientInfo(clientIdx:Long, callback1: (DocumentSnapshot) -> Unit){
        val photoMemoRef = clientDataCollection.document("$clientIdx")
        photoMemoRef.get().addOnSuccessListener(callback1)
    }

    fun getUserPhotoMemoImgUrl(filename: String, callback: (String) -> Unit) {
        val fileRef = storage.reference.child("user${userIdx}/$filename")
        fileRef.downloadUrl.addOnCompleteListener{
            callback(it.result.toString())
        }
    }
}