package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.Transaction
import com.hifi.redeal.transaction.model.TransactionData
import com.hifi.redeal.transaction.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    val transactionList = MutableLiveData<MutableList<Transaction>>()
    private val tempTransactionList = mutableListOf<Transaction>()
    private var newTransactionIdx = 0L

    init {
        getNextTransactionIdx()
        getAllTransactionData()
    }

    fun addDepositTransaction(client: Client, amount: String) {
        val newDepositTransactionData = TransactionData(
            client.getClientIdx(),
            Timestamp.now(),
            true,
            amount,
            newTransactionIdx,
            0,
            "",
            "",
        )
        transactionRepository.setTransactionData(newDepositTransactionData) {
            val newTransaction = Transaction(newDepositTransactionData)
            tempTransactionList.add(newTransaction)
            transactionList.postValue(tempTransactionList)
            getClientName()
            getNextTransactionIdx()
        }
    }

    private fun getAllTransactionData() {
        transactionRepository.getAllTransactionData {
            tempTransactionList.clear()
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

    private fun getNextTransactionIdx() {
        transactionRepository.getNextTransactionIdx {
            for (c1 in it.result) {
                newTransactionIdx = c1["transactionIdx"] as Long + 1L
            }
        }
    }
}
