package com.hifi.redeal.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.model.TransactionData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    var transactionList = MutableLiveData<MutableList<Transaction>>()
    var tempTransactionList = mutableListOf<Transaction>()

    var clientSimpleDataListVM = MutableLiveData<MutableList<ClientSimpleData>>()
    var tempClientSimpleDataList = mutableListOf<ClientSimpleData>()

    var nextTransactionIdx = 0L
    fun getNextTransactionIdx() {
        transactionRepository.getNextTransactionIdx {
            for (c1 in it.result) {
                nextTransactionIdx = c1["transactionIdx"] as Long + 1L
            }
        }
    }

    fun getAllTransactionData() {
        tempTransactionList.clear()
        transactionRepository.getAllTransactionData {
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
                getClientName()
            }
        }
    }

    private fun getClientName() {
        tempTransactionList.forEach { transaction ->
            transactionRepository.getClientInfo(transaction.getTransactionClientIdx()) {
                for (c1 in it.result) {
                    val clientName = c1["clientName"] as String
                    transaction.setTransactionClientName(clientName)
                    transactionList.postValue(tempTransactionList)
                }
            }
        }
    }

    fun getUserAllClient() {
        tempClientSimpleDataList.clear()
        transactionRepository.getUserAllClient() {
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
