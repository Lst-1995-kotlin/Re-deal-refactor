package com.hifi.redeal.transaction.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.hifi.redeal.transaction.model.Client
import com.hifi.redeal.transaction.model.LoadTransactionData
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

    fun addDepositTransaction(client: Client, amount: Long) {
        val createTime = Timestamp.now()
        val newDepositTransactionData = TransactionData(
            client.getClientIdx(),
            createTime,
            true,
            amount,
            newTransactionIdx,
            0,
            0,
            ""
        )
        transactionRepository.setTransactionData(newDepositTransactionData) {
            val newTransaction = Transaction(
                LoadTransactionData(
                    client.getClientIdx(),
                    client.getClientName(),
                    createTime,
                    true,
                    amount,
                    newTransactionIdx,
                    0,
                    0,
                    ""
                )
            )
            totalTransactionData.add(newTransaction)
            updateTransaction()
            getNextTransactionIdx()
        }
    }

    fun addSalesTransaction(
        client: Client,
        itemName: String,
        itemCount: Long,
        itemPrice: Long,
        amount: Long,
    ) {
        val createTime = Timestamp.now()
        val newSalesTransactionData = TransactionData(
            client.getClientIdx(),
            createTime,
            false,
            amount,
            newTransactionIdx,
            itemCount,
            itemPrice,
            itemName
        )
        transactionRepository.setTransactionData(newSalesTransactionData) {
            val newTransaction = Transaction(
                LoadTransactionData(
                    client.getClientIdx(),
                    client.getClientName(),
                    createTime,
                    false,
                    amount,
                    newTransactionIdx,
                    itemCount,
                    itemPrice,
                    itemName
                )
            )
            totalTransactionData.add(newTransaction)
            updateTransaction()
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
                getClientName(
                    c1["clientIdx"] as Long,
                    c1["date"] as Timestamp,
                    c1["isDeposit"] as Boolean,
                    c1["transactionAmountReceived"] as Long,
                    c1["transactionIdx"] as Long,
                    c1["transactionItemCount"] as Long,
                    c1["transactionItemPrice"] as Long,
                    c1["transactionItemName"] as String
                )
            }
        }
    }

    private fun getClientName(
        clientIdx: Long,
        date: Timestamp,
        isDeposit: Boolean,
        transactionAmountReceived: Long,
        transactionIdx: Long,
        transactionItemCount: Long,
        transactionItemPrice: Long,
        transactionItemName: String
    ) {
        transactionRepository.getClientInfo(clientIdx) {
            for (c1 in it.result) {
                totalTransactionData.add(
                    Transaction(
                        LoadTransactionData(
                            clientIdx,
                            c1["clientName"] as String,
                            date,
                            isDeposit,
                            transactionAmountReceived,
                            transactionIdx,
                            transactionItemCount,
                            transactionItemPrice,
                            transactionItemName
                        )
                    )
                )
                updateTransaction()
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
