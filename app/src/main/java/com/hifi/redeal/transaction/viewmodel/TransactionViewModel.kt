package com.hifi.redeal.transaction.viewmodel

import androidx.lifecycle.LiveData
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

    private val totalTransactionData = mutableListOf<Transaction>()
    private val _transactionList = MutableLiveData<List<Transaction>>()
    val transactionList: LiveData<List<Transaction>> get() = _transactionList
    private var newTransactionIdx = 0L
    private var selectClientIndex: Long? = null

    init {
        getNextTransactionIdx()
        getAllTransactionData()
    }

    fun deleteTransactionData(transactionIdx: Long) {
        transactionRepository.deleteTransactionData(transactionIdx) {
            totalTransactionData.removeAll { it.getTransactionIdx() == transactionIdx }
            updateTransaction()
        }
    }

    fun addDepositTransaction(client: Client, amount: String) {
        val newDepositTransactionData = TransactionData(
            client.getClientIdx(),
            Timestamp.now(),
            true,
            amount.replace(",", ""),
            newTransactionIdx,
            0,
            "",
            "",
        )
        transactionRepository.setTransactionData(newDepositTransactionData) {
            val newTransaction = Transaction(newDepositTransactionData)
            totalTransactionData.add(newTransaction)
            getClientName()
            getNextTransactionIdx()
        }
    }

    fun addSalesTransaction(
        client: Client,
        itemName: String,
        itemCount: String,
        itemPrice: String,
        amount: String,
    ) {
        val newReleaseTransactionData = TransactionData(
            client.getClientIdx(),
            Timestamp.now(),
            false,
            amount.replace(",", ""),
            newTransactionIdx,
            itemCount.replace(",", "").toLong(),
            itemPrice.replace(",", ""),
            itemName,
        )
        transactionRepository.setTransactionData(newReleaseTransactionData) {
            val newTransaction = Transaction(newReleaseTransactionData)
            totalTransactionData.add(newTransaction)
            getClientName()
            getNextTransactionIdx()
        }
    }

    fun setSelectClientIndex(clientIndex: Long?) {
        selectClientIndex = clientIndex
        updateTransaction()
    }

    private fun getAllTransactionData() {
        transactionRepository.getAllTransactionData {
            totalTransactionData.clear()
            it.result.forEach { c1 ->
                val transaction = Transaction(
                    TransactionData(
                        c1["clientIdx"] as Long,
                        c1["date"] as Timestamp,
                        c1["isDeposit"] as Boolean,
                        c1["transactionAmountReceived"] as String,
                        c1["transactionIdx"] as Long,
                        c1["transactionItemCount"] as Long,
                        c1["transactionItemPrice"] as String,
                        c1["transactionItemName"] as String,
                    )
                )
                totalTransactionData.add(transaction)
                getClientName()
            }
        }
    }

    private fun getClientName() {
        totalTransactionData.forEach { transaction ->
            if (transaction.isNotSettingClientName()) {
                transactionRepository.getClientInfo(transaction.getTransactionClientIdx()) {
                    for (c1 in it.result) {
                        transaction.setTransactionClientName(c1["clientName"] as String)
                        updateTransaction()
                    }
                }
            }
        }
    }

    private fun updateTransaction() {
        selectClientIndex?.let { index ->
            _transactionList.postValue(totalTransactionData.filter { it.getTransactionClientIdx() == index })
        } ?: _transactionList.postValue(totalTransactionData)
    }

    private fun getNextTransactionIdx() {
        transactionRepository.getNextTransactionIdx {
            for (c1 in it.result) {
                newTransactionIdx = c1["transactionIdx"] as Long + 1L
            }
        }
    }
}
