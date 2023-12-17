package com.hifi.redeal.memo.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.CurrentUserClass
import java.util.Date
import javax.inject.Inject

class RecordMemoRepository @Inject constructor(
    private val currentUser: CurrentUserClass
) {
    private val db = Firebase.firestore
    private val userIdx = currentUser.userIdx
    private val userDataCollection = db.collection("userData")
    private val recordMemoDataCollection = userDataCollection.document(userIdx).collection("recordMemoData")
    fun getRecordMemoAll(clientIdx:Long, callback1: (QuerySnapshot) -> Unit){
        val photoMemoRef = recordMemoDataCollection
            .whereEqualTo("clientIdx", clientIdx)
        photoMemoRef.get()
            .addOnSuccessListener(callback1)
    }

    fun addRecordMemo(clientIdx:Long, recordMemoContext:String, audioFileUri:Uri, audioFileName:String, callback:(Task<Void>) -> Unit){
        val recordMemoRef = recordMemoDataCollection
        recordMemoRef
            .orderBy("recordMemoIdx", Query.Direction.DESCENDING)
            .limit(1)
            .get().addOnSuccessListener{querySnapshot ->
                val recordMemoIdx = if(!querySnapshot.isEmpty){
                    querySnapshot.documents[0].getLong("recordMemoIdx")!! + 1
                }else{
                    1
                }
                val photoMemo = hashMapOf(
                    "clientIdx" to clientIdx,
                    "recordMemoContext" to recordMemoContext,
                    "recordMemoSrc" to audioFileUri,
                    "recordMemoDate" to Timestamp(Date()),
                    "recordMemoIdx" to recordMemoIdx,
                    "recordMemoFilename" to audioFileName
                )
                recordMemoRef.document("$recordMemoIdx").set(photoMemo).addOnCompleteListener(callback)
            }
    }
}