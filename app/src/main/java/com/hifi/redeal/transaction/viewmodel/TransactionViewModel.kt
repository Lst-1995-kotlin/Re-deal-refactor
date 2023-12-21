package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.transaction.model.ClientSimpleData
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.model.TransactionData
import com.hifi.redeal.transaction.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    var transactionList = MutableLiveData<MutableList<Transaction>>()
    var tempTransactionList = mutableListOf<Transaction>()



    fun getAllTransactionData(clientIdx: Long?) {
        tempTransactionList.clear()
        transactionRepository.getAllTransactionData(clientIdx) {
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
        for (transaction in tempTransactionList) {
            if (!transaction.isTransactionClientSetName()) continue
            transactionRepository.getClientInfo(transaction.getTransactionClientIdx()) {
                for (c1 in it.result) {
                    val clientName = c1["clientName"] as String
                    transaction.setTransactionClientName(clientName)
                    transactionList.postValue(tempTransactionList)
                }
            }
        }
    }


}
