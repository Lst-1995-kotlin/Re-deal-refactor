package com.hifi.redeal.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.model.TransactionData

class TransactionViewModel : ViewModel() {

    var transactionList = MutableLiveData<MutableList<Transaction>>()
    var tempTransactionList = mutableListOf<Transaction>()

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
                val transactionData = TransactionData(
                    c1["clientIdx"] as Long,
                    c1["date"] as Timestamp,
                    c1["isDeposit"] as Boolean,
                    c1["transactionAmountReceived"] as String,
                    c1["transactionIdx"] as Long,
                    c1["transactionItemCount"] as Long,
                    c1["transactionItemPrice"] as String,
                    c1["transactionName"] as String,
                )
                val transaction = Transaction(transactionData)
                tempTransactionList.add(transaction)
                transactionList.postValue(tempTransactionList)
                tempTransactionList.forEach { transaction ->
                    getClientName(uid, transaction, transactionData.clientIdx)
                }
            }
        }
    }

    private fun getClientName(
        uid: String,
        transaction: Transaction,
        clientIdx: Long,
    ) {
        TransactionRepository.getClientInfo(uid, clientIdx) {
            for (c1 in it.result) {
                val clientName = c1["clientName"] as String
                transaction.setTransactionClientName(clientName)
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
