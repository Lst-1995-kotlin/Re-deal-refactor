package com.hifi.redeal.trade.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hifi.redeal.CurrentUserClass
import com.hifi.redeal.data.dao.TradeDao
import com.hifi.redeal.trade.data.model.TradeData
import com.hifi.redeal.trade.data.model.TransactionData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val currentUser: CurrentUserClass,
    private val tradeDao: TradeDao
) {
    val trades: Flow<List<TradeData>> = tradeDao.getAllTrade()


    private val uid = currentUser.userIdx
    private val db = Firebase.firestore
    private val dbTransactionRef = db
        .collection("userData")
        .document(uid)
        .collection("transactionData")

    private val dbClientRef = db
        .collection("userData")
        .document(uid)
        .collection("clientData")

    fun deleteTransactionData(transactionIdx: Long, callback: (Task<Void>) -> Unit) {
        dbTransactionRef
            .document("$transactionIdx")
            .delete()
            .addOnCompleteListener(callback)
    }

    fun setTransactionData(
        transactionData: TransactionData,
        callback: (Task<Void>) -> (Unit),
    ) {
        dbTransactionRef
            .document("${transactionData.transactionIdx}")
            .set(transactionData)
            .addOnCompleteListener(callback)
    }

    fun getClientInfo(clientIdx: Long, callback1: (Task<QuerySnapshot>) -> Unit) {
        dbClientRef
            .whereEqualTo("clientIdx", clientIdx)
            .get()
            .addOnCompleteListener(callback1)
    }

    fun getAllTransactionData(callback1: (Task<QuerySnapshot>) -> Unit) {
        dbTransactionRef
            .get()
            .addOnCompleteListener(callback1)
    }

    fun getNextTransactionIdx(callback: (Task<QuerySnapshot>) -> Unit) {
        dbTransactionRef
            .orderBy("transactionIdx", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnCompleteListener(callback)
    }
}
