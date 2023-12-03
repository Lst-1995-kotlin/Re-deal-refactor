package com.hifi.redeal.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.model.ViewTransactionData

class TransactionViewModel : ViewModel() {

    var transactionList = MutableLiveData<MutableList<ViewTransactionData>>()
    var tempTransactionList = mutableListOf<ViewTransactionData>()

    var clientSimpleDataListVM = MutableLiveData<MutableList<ClientSimpleData>>()
    var tempClientSimpleDataList = mutableListOf<ClientSimpleData>()

    var nextTransactionIdx = 0L
    fun getNextTransactionIdx(uid: String) {
        TransactionRepository.getNextTransactionIdx(uid) {
            for (c1 in it.result) {
                nextTransactionIdx = c1["transactionIdx"] as Long + 1L
            }
        }
    }

    fun getAllTransactionData(uid: String) {
        tempTransactionList.clear()
        TransactionRepository.getAllTransactionData(uid) {
            for (c1 in it.result) {
                getClientName(
                    uid,
                    c1["clientIdx"] as Long,
                    c1["date"] as Timestamp,
                    c1["isDeposit"] as Boolean,
                    c1["transactionAmountReceived"] as String,
                    c1["transactionIdx"] as Long,
                    c1["transactionItemCount"] as Long,
                    c1["transactionItemPrice"] as String,
                    c1["transactionName"] as String,
                )
            }
        }
    }

    private fun getClientName(
        uid: String,
        clientIdx: Long,
        date: Timestamp,
        isDeposit: Boolean,
        transactionAmountReceived: String,
        transactionIdx: Long,
        transactionItemCount: Long,
        transactionItemPrice: String,
        transactionName: String,
    ) {
        TransactionRepository.getClientInfo(uid, clientIdx) {
            for (c2 in it.result) {
                val clientName = c2["clientName"] as String
                val newTransactionData = ViewTransactionData(
                    clientIdx,
                    date,
                    isDeposit,
                    transactionAmountReceived,
                    transactionIdx,
                    transactionItemCount,
                    transactionItemPrice,
                    transactionName,
                    clientName,
                )
                tempTransactionList.add(newTransactionData)
                transactionList.postValue(tempTransactionList)
            }
        }
    }

    fun getUserAllClient(uid: String) {
        tempClientSimpleDataList.clear()
        TransactionRepository.getUserAllClient(uid) {
            for (c1 in it.result) {
                val newClientData = ClientSimpleData(
                    c1["clientIdx"] as Long,
                    c1["clientName"] as String,
                    c1["clientManagerName"] as String,
                    c1["clientState"] as Long,
                    c1["isBookmark"] as Boolean,
                )
                tempClientSimpleDataList.add(newClientData)
                clientSimpleDataListVM.postValue(tempClientSimpleDataList)
            }
        }
    }
}
