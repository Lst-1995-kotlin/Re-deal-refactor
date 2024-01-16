package com.hifi.redeal.transaction.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
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

    private val tempTransactionList = mutableListOf<Transaction>()
    private val _transactionList = MutableLiveData<List<Transaction>>()
    val transactionList: LiveData<List<Transaction>> get() = _transactionList
    private var newTransactionIdx = 0L

    init {
        getNextTransactionIdx()
        getAllTransactionData()
    }

    fun deleteTransactionData(transaction: Transaction) {
        transactionRepository.deleteTransactionData(transaction.getTransactionIdx()) {
            _transactionList.value?.indexOfFirst { it == transaction }
                ?.let { it1 -> tempTransactionList.removeAt(it1) }
            _transactionList.postValue(tempTransactionList)
        }
    }

    fun inputValueCheck(textInputEditText: TextInputEditText): Snackbar? {
        if (textInputEditText.text.isNullOrEmpty()) {
            return Snackbar.make(
                textInputEditText,
                "${textInputEditText.hint}(이)가 입력되지 않았습니다.",
                Snackbar.LENGTH_SHORT,
            )
        }
        return null
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
            tempTransactionList.add(newTransaction)
            _transactionList.postValue(tempTransactionList)
            getClientName()
            getNextTransactionIdx()
        }
    }

    fun addReleaseTransaction(
        client: Client,
        itemName: String,
        itemCount: String,
        itemPrice: String,
        amount: String,
    ) {
        val newDepositTransactionData = TransactionData(
            client.getClientIdx(),
            Timestamp.now(),
            false,
            amount.replace(",", ""),
            newTransactionIdx,
            itemCount.replace(",", "").toLong(),
            itemPrice.replace(",", ""),
            itemName,
        )
        transactionRepository.setTransactionData(newDepositTransactionData) {
            val newTransaction = Transaction(newDepositTransactionData)
            tempTransactionList.add(newTransaction)
            _transactionList.postValue(tempTransactionList)
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
                    c1["transactionItemName"] as String,
                )
                val transaction = Transaction(transactionData)
                tempTransactionList.add(transaction)
                _transactionList.postValue(tempTransactionList)
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
                    _transactionList.postValue(tempTransactionList)
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
