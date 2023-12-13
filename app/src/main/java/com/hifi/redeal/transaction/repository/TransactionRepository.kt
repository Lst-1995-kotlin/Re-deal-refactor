package com.hifi.redeal.transaction.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.memo.CurrentUserClass
import com.hifi.redeal.transaction.model.ClientData
import com.hifi.redeal.transaction.model.TransactionData
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val currentUser: CurrentUserClass,
) {

    private val uid = currentUser.userIdx
    private val db = Firebase.firestore
    private val dbTransactionRef = db
        .collection("userData")
        .document(uid)
        .collection("transactionData")

    fun setClientTransactionDataList(
        newTransactionData: TransactionData,
        callback1: (Task<DocumentSnapshot>) -> Unit,
    ) {
        var clientData = ClientData()
        var transactionList = mutableListOf<Long>()
        val db = Firebase.firestore
        db.collection("userData")
            .document(uid)
            .collection("clientData")
            .document("${newTransactionData.clientIdx}")
            .get()
            .addOnSuccessListener {
                clientData = it.toObject<ClientData>()!!
                transactionList = clientData.transactionIdxList?.toMutableList()!!
                transactionList.add(newTransactionData.transactionIdx)
                clientData.transactionIdxList = transactionList.toList()
            }.addOnSuccessListener {
                db.collection("userData")
                    .document(uid)
                    .collection("clientData")
                    .document("${newTransactionData.clientIdx}")
                    .set(clientData)
            }.addOnCompleteListener(callback1)
    }

    fun setTransactionData(
        transactionData: TransactionData,
        callback1: (Task<Void>) -> Unit,
    ) {
        dbTransactionRef
            .document("${transactionData.transactionIdx}")
            .set(transactionData)
            .addOnCompleteListener(callback1)
    }

    fun getClientInfo(clientIdx: Long, callback1: (Task<QuerySnapshot>) -> Unit) {
        val db = Firebase.firestore
        db.collection("userData")
            .document(uid)
            .collection("clientData")
            .whereEqualTo("clientIdx", clientIdx)
            .get()
            .addOnCompleteListener(callback1)
    }

    fun getUserAllClient(callback1: (Task<QuerySnapshot>) -> Unit) {
        val db = Firebase.firestore
        db.collection("userData")
            .document(uid)
            .collection("clientData")
            .get()
            .addOnCompleteListener(callback1)
    }

    fun getNextTransactionIdx(callback: (Task<QuerySnapshot>) -> Unit) {
        dbTransactionRef
            .orderBy("transactionIdx", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnCompleteListener(callback)
    }

    fun getAllTransactionData(clientIdx: Long?, callback1: (Task<QuerySnapshot>) -> Unit) {
        if (clientIdx == null) {
            dbTransactionRef
                .get()
                .addOnCompleteListener(callback1)
            return
        }
        dbTransactionRef
            .whereEqualTo("clientIdx", clientIdx)
            .get()
            .addOnCompleteListener(callback1)
    }
}
